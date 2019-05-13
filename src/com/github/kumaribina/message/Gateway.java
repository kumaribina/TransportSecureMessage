package com.github.kumaribina.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;

import com.github.kumaribina.message.dbo.Message;
import com.github.kumaribina.message.util.CommonUtil;
import com.github.kumaribina.message.util.Constant;
import com.github.kumaribina.message.util.EncryptUtil;
import com.github.kumaribina.message.util.SignUtil;
import com.sun.mail.util.LineOutputStream;

public class Gateway {

	private Properties properties;
	public Gateway(Properties properties) {
		this.properties = properties; 
		CommonUtil.init();
	}
	public void createMessage(Message message, String payload, String contentType, String filePath) {
		try {
			
			//initialize  the Mime session 
			Properties props = System.getProperties();
			Session session = Session.getDefaultInstance(props, null);
			
			MimeMultipart multipart = new MimeMultipart(Constant.RELATED);
			
			//convert payload into MimeBodyPart
			if (payload != null) {
				MimeBodyPart payloadPart = new MimeBodyPart();
				DataHandler handler = new DataHandler(new ByteArrayDataSource(payload, contentType));
				payloadPart.setDataHandler(handler);
				payloadPart.setHeader(Constant.CONTENT_TYPE, contentType);
				//payloadPart.setHeader(Constant.CONTENT_TRANSFER_ENCODING, Constant.BASE64);
				multipart.addBodyPart(payloadPart);
			}
			
			//convert attachment into MimeBodyPart
			if (filePath != null) {
				Tika tika = new Tika();
				File file = new File(filePath);
				String mimeType = tika.detect(file);
				InputStream inStream = new FileInputStream(file);
				DataHandler handler = new DataHandler(new ByteArrayDataSource(inStream, mimeType));
				
				MimeBodyPart attachPart = new MimeBodyPart();
				attachPart.setDataHandler(handler);
				attachPart.setHeader(Constant.CONTENT_TYPE, mimeType);
				attachPart.setHeader(Constant.CONTENT_TRANSFER_ENCODING, Constant.BASE64);
				attachPart.setFileName(file.getName());
				multipart.addBodyPart(attachPart);
			}
			
			//convert part to MimeMessage
			Part contentPart = null;
			contentPart = new MimeMessage(session);
			contentPart.setContent(multipart, multipart.getContentType());
	        ((MimeMessage) contentPart).saveChanges();
			
			//sign the message
	        
	        String signingKeyStore = properties.getProperty("signing.keystore.path");
	        String signingKeyPass = properties.getProperty("signing.keystore.pass");
	        String signingKeyAlias = properties.getProperty("signing.keystore.alias");
			MimeMessage signedMime = SignUtil.sign((MimeMessage)contentPart, session, signingKeyStore, signingKeyPass, signingKeyAlias);
			ByteArrayOutputStream signedOut = getStreamFromMime(signedMime);
			ByteArrayInputStream signedStream = new ByteArrayInputStream( signedOut.toByteArray() );
			message.setSigned(IOUtils.toByteArray(signedStream));
			
			//encrypt the message
			String encryptionCert = properties.getProperty("encrypt.cert.path");
			MimeMessage encrypted = EncryptUtil.encrypt(signedMime, session, encryptionCert);
			ByteArrayOutputStream encryptOut = getStreamFromMime(encrypted);
			ByteArrayInputStream encryptedStream = new ByteArrayInputStream( encryptOut.toByteArray() );
			message.setEncrypted(IOUtils.toByteArray(encryptedStream));
			message.setContentType(encrypted.getContentType());
			
		} catch (Exception e) {
			message.setErrorString(e.toString());
		}
	}
	
	public void processMessage(Message message) throws IOException {
		try {
			
			Session session = Session.getDefaultInstance(System.getProperties());
			ByteArrayOutputStream decrypted = new ByteArrayOutputStream();
			
			MimeMessage mimeMessage = new MimeMessage(session, new ByteArrayInputStream(message.getEncrypted()));
			
			String decryptKeyStore = properties.getProperty("decrypt.keystore.path");
	        String decryptKeyPass = properties.getProperty("decrypt.keystore.pass");
	        String decryptKeyAlias = properties.getProperty("decrypt.keystore.alias");
			EncryptUtil.decrypt(mimeMessage.getInputStream(), decrypted, decryptKeyStore, decryptKeyPass, decryptKeyAlias);
			
			String verificationCert = properties.getProperty("verify.cert.path");
			message.setSigned(IOUtils.toByteArray(new ByteArrayInputStream( decrypted.toByteArray() )));
			SignUtil.verifySignature(decrypted, Session.getDefaultInstance(System.getProperties(), null), verificationCert);
			//System.out.println(contentType);
			///System.out.println(IOUtils.toString(new ByteArrayInputStream( verified.toByteArray() )));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static ByteArrayOutputStream getStreamFromMime(MimeMessage message) throws IOException, MessagingException {
		ByteArrayOutputStream signedOut = new ByteArrayOutputStream();
		
		Enumeration<String> headerLines = message.getMatchingHeaderLines(new String[]{Constant.CONTENT_TYPE});
        LineOutputStream los = new LineOutputStream(signedOut);
        while (headerLines.hasMoreElements()) {
            String nextHeaderLine = MimeUtility.unfold((String) headerLines.nextElement());
            los.writeln(nextHeaderLine);
        }
        los.close();
        message.writeTo(signedOut, new String[]{Constant.MESSAGE_ID, Constant.MIME_VERSION, Constant.CONTENT_TYPE});
        signedOut.flush();
        signedOut.close();
        
        return signedOut;
	}
}
