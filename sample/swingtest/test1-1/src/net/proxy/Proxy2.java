package net.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Proxy2 
{
    private static String PROXY_HOST = "host";
    private static String PROXY_PORT = "port";
    private static String PROXY_USER = "user";
    private static String PROXY_PASSWORD = "password";

    public static void main(String[] args) throws Exception {
            //String url = "https://news.google.com";
            //String url = "http://www.livedoor.com/";
    		String url = "https://sourceforge.net/projects/miludbviewer/rss?path=/";
            //System.setProperty("proxySet", "true");
            System.setProperty("proxyHost", PROXY_HOST);
            System.setProperty("proxyPort", PROXY_PORT);
            
    		System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    		System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");         

            Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
                    }
            });

            HttpURLConnection urlconn = (HttpURLConnection) new URL(url).openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            
    		StringBuffer page = new StringBuffer();
    		BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
    		String line;
    		while ( (line = in.readLine()) != null )
    		{
    		   page.append( line + "\n" );
    		}
    		
    		System.out.println( page.toString() );
    }
}
