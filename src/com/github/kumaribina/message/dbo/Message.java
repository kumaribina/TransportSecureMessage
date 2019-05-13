package com.github.kumaribina.message.dbo;

public class Message {

	private String date;
	private String sender;
	private String receiver;
	private String messageid;
	
	private byte[] signed;
	private byte[] encrypted;
	
	private String contentType;
	
	private String errorString;

	public Message() {

	}

	public Message(String date, String sender, String receiver, String messageid) {
		this.date = date;
		this.sender = sender;
		this.receiver = receiver;
		this.messageid = messageid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public byte[] getSigned() {
		return signed;
	}

	public void setSigned(byte[] signed) {
		this.signed = signed;
	}

	public byte[] getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(byte[] encrypted) {
		this.encrypted = encrypted;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
