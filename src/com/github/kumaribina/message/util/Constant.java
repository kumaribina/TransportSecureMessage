package com.github.kumaribina.message.util;

public class Constant {

	private Constant() {
		
	}
	public static final String ATTACHMENT_PATH = "C:/Users/BKUM/Desktop/one.txt";
	
	public static final String SIGNING_KEYSTORE_PATH = "C:/Users/BKUM/Documents/Office/Certificate/webMethods_OpenSSL_Cert/Partner1/p1.jks";
	public static final String SIGNING_KEYSTORE_PASS = "p1";
	public static final String SIGNING_KEYSTORE_ALIAS = "partner1";
	
	public static final String VERIFY_CERT_PATH = "C:/Users/BKUM/Documents/Office/Certificate/webMethods_OpenSSL_Cert/partner1cert.der";
	
	
	public static final String ENCRYPT_CERT_PATH = "C:/Users/BKUM/Documents/Office/Certificate/webMethods_OpenSSL_Cert/partner2cert.der";
	public static final String DECRYPT_CERT_PATH = "C:/Users/BKUM/Documents/Office/Certificate/webMethods_OpenSSL_Cert/Partner2/p2.jks";
	public static final String DECRYPT_KEYSTORE_ALIAS = "partner2 (webm test ca)";
	public static final String DECRYPT_KEYSTORE_PASS = "p2";
	
	public static final String BASE64 = "base64";
	public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	public static final String X_509 = "X.509";
	public static final String BC = "BC";
	public static final String APPLICATION_PKCS7_MIME = "application/pkcs7-mime";
	public static final String MULTIPART_SIGNED = "multipart/signed";
	public static final String SMIME_TYPE = "smime-type";
	public static final String ENVELOPED_DATA = "enveloped-data";
	public static final String SHA1WITHRSA = "SHA1WITHRSA";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String MIME_VERSION = "Mime-Version";
	public static final String MESSAGE_ID = "Message-ID";
	public static final String RELATED = "related";
	public static final String UTF_8 = "UTF-8";
}
