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
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSTypedStream;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;

public class EncryptUtil {
	
	private EncryptUtil() {
		
	}
	
	public static MimeMessage encrypt(MimeMessage bodyPart, Session session, String certPath) throws SMIMEException, CMSException, MessagingException, IOException, CertificateException {
		// loading public cert of partner
		CertificateFactory fact = CertificateFactory.getInstance(Constant.X_509);
		FileInputStream is = new FileInputStream(certPath);
		X509Certificate cert = (X509Certificate) fact.generateCertificate(is);
		// create the encryptor
		SMIMEEnvelopedGenerator encryptor = new SMIMEEnvelopedGenerator();
		encryptor.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(cert).setProvider(Constant.BC));
		encryptor.setContentTransferEncoding(Constant.BASE64);
		JceCMSContentEncryptorBuilder jceCMSContentEncryptorBuilder = 
		  new JceCMSContentEncryptorBuilder(new ASN1ObjectIdentifier(SMIMEEnvelopedGenerator.DES_EDE3_CBC)).setProvider(Constant.BC);
		jceCMSContentEncryptorBuilder.setSecureRandom(new SecureRandom());
		// encrypting
		MimeBodyPart encryptedPart = encryptor.generate(bodyPart, jceCMSContentEncryptorBuilder.build());
		// setting encrypted content
		MimeMessage finalMessage = new MimeMessage(session);
		finalMessage.setContent(encryptedPart.getContent(), encryptedPart.getContentType());
		finalMessage.setHeader(Constant.CONTENT_TRANSFER_ENCODING, Constant.BASE64);
		finalMessage.saveChanges();
		
		return finalMessage;
	}
	
	public static void decrypt(InputStream encrypted, OutputStream decrypted, String keyStore, String keyPass, String keyAlias) throws Exception {

		KeyStore keystore = CommonUtil.getKeyStore(keyStore, keyPass);

		Key key = keystore.getKey(keyAlias, keyPass.toCharArray());
		// Cast parameters to what BC needs
		X509Certificate x509Cert = (X509Certificate) keystore.getCertificate(keyAlias);
		BufferedInputStream bufferedEncrypted = new BufferedInputStream(encrypted);
		BufferedOutputStream bufferedDecrypted = new BufferedOutputStream(decrypted);
		
		CMSEnvelopedDataParser parser = new CMSEnvelopedDataParser(bufferedEncrypted);
		RecipientId recipientId = new JceKeyTransRecipientId(x509Cert);
		RecipientInformation recipient = parser.getRecipientInfos().get(recipientId);
		if (recipient != null) {
			CMSTypedStream cmsEncrypted = recipient
					.getContentStream(new JceKeyTransEnvelopedRecipient(CommonUtil.getPrivateKey(key)).setProvider("BC"));
			InputStream encryptedContent = cmsEncrypted.getContentStream();
			CommonUtil.copyStreams(encryptedContent, bufferedDecrypted);
			bufferedDecrypted.flush();
		} else {
			throw new GeneralSecurityException("Wrong key used to decrypt the data.");
		}
	}
	 
	 
	public static boolean isEncrypted(MimeBodyPart part) throws MessagingException
    {
        ContentType contentType = new ContentType(part.getContentType());
        String baseType = contentType.getBaseType().toLowerCase();

        if (baseType.equalsIgnoreCase(Constant.APPLICATION_PKCS7_MIME))
        {
            String smimeType = contentType.getParameter(Constant.SMIME_TYPE);
            boolean checkResult = (smimeType != null) && smimeType.equalsIgnoreCase(Constant.ENVELOPED_DATA);
            
            return (checkResult);
        }
        
        return false;
    }
	
	
}
