package milu.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import java.security.SecureRandom;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;

public class MySecurityKey 
{
	enum MODE
	{
		ENCRYPT,
		DECRYPT
	};
	
	private static int KEY_SIZE = 256;
	public static String ALGORITHM = "AES_256/CBC/NOPADDING";
	private static String CIPHER = "AES";
	private static String char_code = "UTF-8";
	
	private SecretKey secretKey = null;
	private byte[]    iv        = null;
	
	public SecretKey createKey() throws NoSuchAlgorithmException
	{
		KeyGenerator keygen = KeyGenerator.getInstance(CIPHER);
		SecureRandom random = new SecureRandom();
		keygen.init( KEY_SIZE, random );
		return keygen.generateKey();
	}
	
	public byte[] createIV() throws NoSuchAlgorithmException
	{
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] iv = new byte[16];
		random.nextBytes(iv);
		return iv;
	}
	
	public String encrypt( SecretKey secretKey, byte[] iv, String plainText )
			throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException
	{
		this.secretKey = secretKey;
		this.iv        = iv;
		
    	byte[] plainBytes = plainText.getBytes();
    	int plainSize = plainBytes.length*8;
    	int blockSize = plainSize;
    	int addSize   = 0;
    	if ( blockSize%KEY_SIZE != 0 )
    	{
    		addSize = KEY_SIZE-blockSize%KEY_SIZE;
    		blockSize += addSize;
    	}
    	byte[] dataBytes  = new byte[blockSize/8];
    	System.arraycopy( plainBytes, 0, dataBytes, 0, plainSize/8 );
    	// --------------------------------------------------
    	// PKCS5Padding
    	// --------------------------------------------------
    	for ( int i = plainSize/8; i < blockSize/8; i++ )
    	{
    		dataBytes[i] = (byte)(addSize/8);
    	}
		
    	byte[] cipher_byte = this.crypt( dataBytes, MODE.ENCRYPT );
        String b64 = Base64.getEncoder().encodeToString(cipher_byte);
        return b64;
	}
	
	public String decrypt(String text64)
		throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException,
			UnsupportedEncodingException
	{
        byte[] cipher_byte = Base64.getDecoder().decode(text64.getBytes() );
        byte[] plain_byte = this.crypt( cipher_byte, MODE.DECRYPT );
        
        // Get last byte for PKCS5Padding
        byte lastByte = plain_byte[plain_byte.length-1];
        if ( ( lastByte > 0 ) && ( lastByte < KEY_SIZE/8 ) )
        {
        }
        else
        {
        	lastByte = 0;
        }
        
        int  copySize = plain_byte.length-lastByte;
        byte[] orgByte =  new byte [copySize];
        System.arraycopy( plain_byte, 0, orgByte, 0, copySize );
        
        String plaintext = new String( orgByte, char_code );
        return plaintext;
    }
	
	private byte[] crypt( byte[] input, MODE mode ) 
		throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException
	{
		byte[] output = null;
		
		byte[] key = this.secretKey.getEncoded();
		SecretKeySpec skey = new SecretKeySpec(key, CIPHER);
		IvParameterSpec ivp = new IvParameterSpec(this.iv);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
        int m = (mode == MODE.ENCRYPT)
                ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        cipher.init( m, skey, ivp );
		output = cipher.doFinal(input);
		return output;
	}
}
