package com.github.kumaribina.message.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Header;
import javax.servlet.ServletContext;

public class CommonUtil {

	private CommonUtil() {
		
	}

	public static X509Certificate castCertificate(Certificate cert) throws GeneralSecurityException {
		if (cert == null) {
			throw new GeneralSecurityException("Certificate is null");
		}
		if (!(cert instanceof X509Certificate)) {
			throw new GeneralSecurityException("Certificate must be an instance of X509Certificate");
		}
		return (X509Certificate) cert;
	}

	public static String printHeaders(Enumeration<Header> hdrs) {
		return printHeaders(hdrs, " == ", "\n\t\t");
	}

	public static String printHeaders(Enumeration<Header> hdrs, String nameValueSeparator, String valuePairSeparator) {
		String headers = "";
		while (hdrs.hasMoreElements()) {
			Header h = hdrs.nextElement();
			headers = headers + valuePairSeparator + h.getName() + nameValueSeparator + h.getValue();
		}
		return (headers);
	}
	
	public static void init() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	
	public static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore identityKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
		identityKeystore.load(is, password.toCharArray());
		
		return identityKeystore;
		
	}
	
	public static void copyStreams(InputStream in, OutputStream out) throws IOException {
	        BufferedInputStream inStream = new BufferedInputStream(in);
	        BufferedOutputStream outStream = new BufferedOutputStream(out);
	        //copy the contents to an output stream
	        byte[] buffer = new byte[2048];
	        int read = 2048;
	        //a read of 0 must be allowed, sometimes it takes time to
	        //extract data from the input
	        while (read != -1) {
	            read = inStream.read(buffer);
	            if (read > 0) {
	                outStream.write(buffer, 0, read);
	            }
	        }
	        outStream.flush();
	    }
	
	public static PrivateKey getPrivateKey(Key key) throws GeneralSecurityException {
        if (key == null) {
            throw new GeneralSecurityException("getPrivateKey: Key is null");
        }
        if (!(key instanceof PrivateKey)) {
            throw new GeneralSecurityException("getPrivateKey: Key must implement PrivateKey interface");
        } else {
            return (PrivateKey) key;
        }
    }
	
	private static long currentMessageId = 0L;
    private static long currentId = System.currentTimeMillis();

    public static synchronized String createMessageId(String senderId, String receiverId) {
        StringBuilder idBuffer = new StringBuilder();
        idBuffer.append("SecMessageServer");
        idBuffer.append("-");
        idBuffer.append(String.valueOf(System.currentTimeMillis()));
        idBuffer.append("-");
        idBuffer.append(String.valueOf(currentMessageId++));
        idBuffer.append("@");
        if (senderId != null) {
            idBuffer.append(senderId);
        } else {
            idBuffer.append("unknown");
        }
        idBuffer.append("_");
        if (receiverId != null) {
            idBuffer.append(receiverId);
        } else {
            idBuffer.append("unknown");
        }
        return (idBuffer.toString());
    }

    /**Creates a new id in the format yyyyMMddHHmm-nn*/
    public static synchronized String createId(){        
        StringBuilder idBuffer = new StringBuilder();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        idBuffer.append( format.format( new Date()));
        idBuffer.append( "-" );
        idBuffer.append( currentId++ );
        return( idBuffer.toString() );
    }
    
    public static Properties getProperties(ServletContext context) {
    	//load the properties
        InputStream input = context.getResourceAsStream("/WEB-INF/config.properties");
        Properties properties = new Properties();
        try {
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return properties;
    }
}
