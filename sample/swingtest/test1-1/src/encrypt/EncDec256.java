package encrypt;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

// http://kaworu.jpn.org/java/%E5%85%B1%E9%80%9A%E9%8D%B5%E6%9A%97%E5%8F%B7%E6%96%B9%E5%BC%8F%E3%81%AEAES%E3%81%AB%E3%82%88%E3%82%8B%E6%9A%97%E5%8F%B7%E5%8C%96%E3%81%A8%E5%BE%A9%E5%8F%B7%E5%8C%96
public class EncDec256 extends Application 
{
    enum MODE 
    {
        ENCRYPT,
        DECRYPT
    };
    
	public static SecretKey ENCRYPT_KEY;
	public static int KEY_SIZE = 256;
	public static byte[] ENCRYPT_IV;
	public static String ALGORITHM = "AES_256/CBC/NOPADDING";
	//public static String ALGORITHM = "AES_256/CBC/PKCS5Padding";
	public static String CIPHER = "AES";
	public static String char_code = "UTF-8";

	@Override
	public void start(Stage stage) throws Exception 
	{
		Label      lblInput = new Label("Input");
		TextField  txtInput = new TextField("HogehogeHogehoge");
		txtInput.setPrefWidth(450);
		
		Button     btnGo    = new Button("Go");
		
		Label      lblEnc   = new Label("Encrypt");
		TextField  txtEnc   = new TextField();
		txtEnc.setPrefWidth(450);
		
		Label      lblDec   = new Label("Decrypt");
		TextField  txtDec   = new TextField();
		txtDec.setPrefWidth(450);
		
		GridPane   gridPane = new GridPane();
		gridPane.add( lblInput, 0 , 0 );
		gridPane.add( txtInput, 1 , 0 );
		gridPane.add( btnGo   , 1 , 1 );
		gridPane.add( lblEnc  , 0 , 2 );
		gridPane.add( txtEnc  , 1 , 2 );
		gridPane.add( lblDec  , 0 , 3 );
		gridPane.add( txtDec  , 1 , 3 );
		
		btnGo.setOnAction
		(
			(event)->
			{
				try
				{
					String strEnc = encrypt(txtInput.getText());
					txtEnc.setText(strEnc);
					String strDec = decrypt(strEnc);
					txtDec.setText(strDec);
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
				}
			}
		);
		
		Scene scene = new Scene( gridPane, 650, 250 );
		stage.setScene(scene);
		stage.show();		
	}

    public static String encrypt (String plaintext) throws Exception {
    	System.out.println( "====== encrypt ======" );
        //byte[] cipher_byte = crypt(plaintext.getBytes(), MODE.ENCRYPT);
    	
    	byte[] plainBytes = plaintext.getBytes();
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
    	for ( int i = plainSize/8; i < blockSize/8; i++ )
    	{
    		dataBytes[i] = (byte)(addSize/8);
    	}
    	
    	System.out.println( "plainSize:" + plainSize );
    	System.out.println( "blockSize:" + blockSize );
    	System.out.println( "addSize  :" + addSize );
    	
    	byte[] cipher_byte = crypt(dataBytes, MODE.ENCRYPT);
        String b64 = Base64.getEncoder().encodeToString(cipher_byte);
        return b64;
	}
    
	public static String decrypt (String text64) throws Exception {
		System.out.println( "====== decrypt ======" );
        byte[] cipher_byte = Base64.getDecoder().decode(text64.getBytes() );
        byte[] plain_byte = crypt(cipher_byte, MODE.DECRYPT);
        //String plaintext = new String(plain_byte, char_code);
        
        byte lastByte = plain_byte[plain_byte.length-1];
        if ( ( lastByte > 0 ) && ( lastByte < KEY_SIZE/8 ) )
        {
        }
        else
        {
        	lastByte = 0;
        }
		System.out.println( "lastByte :" + lastByte );
        
        int  copySize = plain_byte.length-lastByte;
        byte[] orgByte =  new byte [copySize];
        System.arraycopy( plain_byte, 0, orgByte, 0, copySize );
        
        String plaintext = new String( orgByte, char_code);
        return plaintext;
	}
	
	public static byte[] crypt(byte[] input, MODE mode) throws Exception {
        byte[] output;
        try {
            byte[] key = ENCRYPT_KEY.getEncoded();
            byte[] iv = ENCRYPT_IV;

            SecretKeySpec skey = new SecretKeySpec(key, CIPHER);

            IvParameterSpec ivp = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            int m = (mode == MODE.ENCRYPT)
                    ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
            cipher.init(m, skey, ivp);
            output = cipher.doFinal(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Can not crypt");
        }
        return output;
	}
	
	public static void makeKey () throws Exception {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(CIPHER);
            SecureRandom random = new SecureRandom();
            keygen.init(KEY_SIZE, random);
            ENCRYPT_KEY = keygen.generateKey();
        } catch (Exception e) {
            throw new Exception("Can not make key", e);
        }
	}
	
	public static void makeIv () throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        ENCRYPT_IV = new byte[16];
        random.nextBytes(ENCRYPT_IV);
	}

	public static void main( String[] args )
	{
		try
		{
			makeKey();
	        makeIv();
	        launch(args);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
