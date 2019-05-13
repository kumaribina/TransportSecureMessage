package com.github.kumaribina.message.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.mail.smime.SMIMESignedParser;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class SignUtil {

	private SignUtil() {
		
	}
	
	
	public static MimeMessage sign(MimeMessage bodyPart, Session session, String keyStore, String keyPass, String keyAlias)
			throws Exception {

		// loading identity store
		KeyStore identityKeystore = CommonUtil.getKeyStore(keyStore, keyPass);
		// extracting certificate from identity store
		X509Certificate signCert = (X509Certificate) identityKeystore.getCertificate(keyAlias);
		List<X509Certificate> certList = new ArrayList<>();
		certList.add(signCert);
		Store<?> certs = new JcaCertStore(certList);
		// create the generator for creating an smime/signed message
		SMIMESignedGenerator signer = new SMIMESignedGenerator();
		signer.setContentTransferEncoding(Constant.BASE64);
		// extracting private key from identity store
		Key key = identityKeystore.getKey(keyAlias, keyPass.toCharArray());
		KeyPair keyPair;
		if (key instanceof PrivateKey) {
			Certificate cert = identityKeystore.getCertificate(keyAlias);
			PublicKey publicKey = cert.getPublicKey();
			keyPair = new KeyPair(publicKey, (PrivateKey) key);
		} else {
			throw new UnrecoverableKeyException("Identity store does not contain keypair for alias " + keyAlias);
		}
		// add a signer to the generator
		signer.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder().setProvider(Constant.BC).build(Constant.SHA1WITHRSA,
				keyPair.getPrivate(), signCert));
		// add our pool of certs and certs (if any) to go with the signature
		signer.addCertificates(certs);
		MimeMultipart signedMimeMultipart = signer.generate(bodyPart);
		MimeMessage finalMessage = new MimeMessage(session);
		// set the content of the signed message
		finalMessage.setContent(signedMimeMultipart);
		finalMessage.saveChanges();

		return finalMessage;

	}

	public static boolean isSigned(MimeBodyPart part) throws MessagingException {
		ContentType contentType = new ContentType(part.getContentType());
		String baseType = contentType.getBaseType().toLowerCase();

		return baseType.equalsIgnoreCase(Constant.MULTIPART_SIGNED);
	}
	
	public static ByteArrayOutputStream verifySignature(ByteArrayOutputStream signedOut, Session session, String certPath) throws Exception {
		MimeBodyPart receivedPart = new MimeBodyPart();
		receivedPart.setDataHandler(
				new DataHandler(new ByteArrayDataSource(signedOut.toByteArray(), "multipart/signed")));
		
		receivedPart.setHeader("Content-Type", "multipart/signed");
		
		
		CertificateFactory fact = CertificateFactory.getInstance(Constant.X_509);
		FileInputStream is = new FileInputStream(certPath);
		X509Certificate cert = (X509Certificate) fact.generateCertificate(is);
		
		
		MimeBodyPart partttt = SignUtil.verifySignature(receivedPart, cert);
		
		MimeMessage finalMessage = new MimeMessage(session);
		finalMessage.setContent(partttt.getContent(), partttt.getContentType());
		
		ByteArrayOutputStream verified = new ByteArrayOutputStream();
		finalMessage.writeTo(verified, new String[]{Constant.MESSAGE_ID, Constant.MIME_VERSION, Constant.CONTENT_TYPE});
		verified.flush();
		verified.close();
		//System.out.println(IOUtils.toString(verified.toByteArray(), Constant.UTF_8));
		return verified;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private static MimeBodyPart verifySignature(MimeBodyPart part, Certificate cert)
			throws GeneralSecurityException, IOException, MessagingException, CMSException, OperatorCreationException {
		// Make sure the data is signed
		if (!isSigned(part)) {
			throw new GeneralSecurityException("Content-Type indicates data isn't signed");
		}

		X509Certificate x509Cert = CommonUtil.castCertificate(cert);

		MimeMultipart mainParts = (MimeMultipart) part.getContent();

		SMIMESigned signedPart = new SMIMESigned(mainParts);

		DigestCalculatorProvider dcp = new JcaDigestCalculatorProviderBuilder().setProvider(Constant.BC).build();
		String contentTxfrEnc = signedPart.getContent().getEncoding();
		if (contentTxfrEnc == null || contentTxfrEnc.length() < 1) {
			contentTxfrEnc = Constant.BASE64;
		}
		SMIMESignedParser ssp = new SMIMESignedParser(dcp, mainParts, contentTxfrEnc);
		SignerInformationStore sis = ssp.getSignerInfos();

		CommonUtil.printHeaders(part.getAllHeaders());
		CommonUtil.printHeaders(ssp.getContent().getAllHeaders());

		Iterator<SignerInformation> it = sis.getSigners().iterator();
		SignerInformationVerifier signerInfoVerifier = new JcaSimpleSignerInfoVerifierBuilder().setProvider(Constant.BC)
				.build(x509Cert);
		while (it.hasNext()) {
			SignerInformation signer = it.next();

			try { // Code block below does not do null-checks or other encoding error checking.
				Map<Object, Attribute> attrTbl = signer.getSignedAttributes().toHashtable();
				StringBuilder strBuf = new StringBuilder();
				for (Map.Entry<Object, Attribute> pair : attrTbl.entrySet()) {
					strBuf.append("\n\t").append(pair.getKey()).append(":=");
					ASN1Encodable[] asn1s = pair.getValue().getAttributeValues();
					for (int i = 0; i < asn1s.length; i++) {
						strBuf.append(asn1s[i]).append(";");
					}
				}

				AttributeTable attributes = signer.getSignedAttributes();
				Attribute attribute = attributes.get(CMSAttributes.messageDigest);
				DEROctetString digest = (DEROctetString) attribute.getAttrValues().getObjectAt(0);

			} catch (Exception e) {
			}
			if (signer.verify(signerInfoVerifier)) {

				return signedPart.getContent();
			}

		}
		throw new SignatureException("Signature Verification failed");
	}
}
