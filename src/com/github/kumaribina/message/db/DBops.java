package com.github.kumaribina.message.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.kumaribina.message.dbo.Message;
import com.github.kumaribina.message.dbo.Users;

public class DBops {

	private static final String SENT_MESSAGE_QUERY = "Select a.Date, a.Sender, a.Receiver, a.Messageid from Sent_Message a ";
	private static final String RECEIVED_MESSAGE_QUERY = "Select a.Date, a.Sender, a.Receiver, a.Messageid from Received_Message a ";
	
	private static final String SENT_MESSAGE_INSERT = "Insert into SENT_MESSAGE(Date, Sender, Receiver,Messageid,Signed,Encrypted) values (?,?,?,?,?,?)";
	private static final String RECEIVED_MESSAGE_INSERT = "Insert into RECEIVED_MESSAGE(Date, Sender, Receiver,Messageid,Signed,Encrypted) values (?,?,?,?,?,?)";

	private static final String FIND_SENT_MESSAGE_QUERY = "Select a.Date, a.Sender, a.Receiver, a.Messageid, a.Signed, a.Encrypted from Sent_Message a where a.Messageid=?";
	private static final String FIND_RECEIVED_MESSAGE_QUERY = "Select a.Date, a.Sender, a.Receiver, a.Messageid, a.Signed, a.Encrypted from Received_Message a where a.Messageid=?";
	
	
	private DBops() {
		
	}
	
	public static Users findUser(Connection conn, //
            String userName, String password) throws SQLException {
 
        String sql = "Select a.User_Name, a.Password, a.Gender, a.First_Name, a.Last_Name from User_Account a " //
                + " where a.User_Name = ? and a.password= ?";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
        pstm.setString(2, password);
        ResultSet rs = pstm.executeQuery();
 
        if (rs.next()) {
            String firstName = rs.getString("First_Name");
            String lastName = rs.getString("Last_Name");
            String gender = rs.getString("Gender");
            Users user = new Users();
            user.setUserName(userName);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setGender(gender);
            return user;
        }
        return null;
    }
 
    public static Users findUser(Connection conn, String userName) throws SQLException {
 
        String sql = "Select a.User_Name, a.Password, a.Gender, a.First_Name, a.Last_Name from User_Account a "//
                + " where a.User_Name = ? ";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
 
        ResultSet rs = pstm.executeQuery();
 
        if (rs.next()) {
            String password = rs.getString("Password");
            String gender = rs.getString("Gender");
            String firstName = rs.getString("First_Name");
            String lastName = rs.getString("Last_Name");
            Users user = new Users();
            user.setUserName(userName);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setGender(gender);
            return user;
        }
        return null;
    }
    
    public static List<Message> querySentMessage(Connection conn, boolean isSent) throws SQLException {
        String sql = SENT_MESSAGE_QUERY;
        if (!isSent) {
        	sql = RECEIVED_MESSAGE_QUERY;
        }
        PreparedStatement pstm = conn.prepareStatement(sql);
 
        ResultSet rs = pstm.executeQuery();
        List<Message> list = new ArrayList<Message>();
        while (rs.next()) {
            String date = rs.getString("Date");
            String sender = rs.getString("Sender");
            String receiver = rs.getString("Receiver");
            String messageid = rs.getString("Messageid");
            Message message = new Message();
            message.setDate(date);
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageid(messageid);
            list.add(message);
        }
        return list;
    }
    
    public static Message findMessageByID(Connection conn, String id, boolean isSent) throws SQLException {
    	String sql = FIND_SENT_MESSAGE_QUERY;
        if (!isSent) {
        	sql = FIND_RECEIVED_MESSAGE_QUERY;
        }
 
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, id);
 
        ResultSet rs = pstm.executeQuery();
 
        while (rs.next()) {
        	String date = rs.getString("Date");
            String sender = rs.getString("Sender");
            String receiver = rs.getString("Receiver");
            String messageid = rs.getString("Messageid");
            
            
            Message message = new Message(date, sender, receiver, messageid);
            message.setSigned(readBlob(rs, "Signed"));
			message.setEncrypted(readBlob(rs, "Encrypted"));
            return message;
        }
        return null;
    }
 
    public static void insertMessage(Connection conn, boolean isSent, Message message) throws SQLException {
    	 String sql = SENT_MESSAGE_INSERT;
         if (!isSent) {
         	sql = RECEIVED_MESSAGE_INSERT;
         }
 
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, message.getDate());
        pstm.setString(2, message.getSender());
        pstm.setString(3, message.getReceiver());
        pstm.setString(4, message.getMessageid());

        pstm.setBinaryStream(5, new ByteArrayInputStream(message.getSigned()));
        pstm.setBinaryStream(6, new ByteArrayInputStream(message.getEncrypted()));
        
        pstm.executeUpdate();
    }
    
    public static byte[] readBlob(ResultSet resultSet, String column) {
        final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            final InputStream binaryStream = resultSet.getBinaryStream(column);
            final byte[] buffer = new byte[1024];
            while (binaryStream.read(buffer) > 0) {
                arrayOutputStream.write(buffer);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayOutputStream.toByteArray();
    }
}
