package com.github.kumaribina.message.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.github.kumaribina.message.Gateway;
import com.github.kumaribina.message.db.DBUtils;
import com.github.kumaribina.message.db.DBops;
import com.github.kumaribina.message.dbo.Message;
import com.github.kumaribina.message.util.CommonUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(urlPatterns = { "/send" })
public class SendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public SendServlet() {
        super();
    }
 
    // Show send page.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/send.jsp");
        dispatcher.forward(request, response);
    }
 
    // When the user enters the Send information, and click Submit.
    // This method will be called.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = DBUtils.getStoredConnection(request);
 
        String sender = request.getParameter("sender");
        String receiver = request.getParameter("receiver");
        String paylod = request.getParameter("paylod");
        String contentType = request.getParameter("contentType");
        
       //Handle file
        Properties properties = CommonUtil.getProperties(this.getServletContext());
        

        String uploadDir = properties.getProperty("attachment.temp.path");
        
        String applicationPath = getServletContext().getRealPath(""),
        		uploadPath = applicationPath + File.separator + uploadDir;
        
        File fileUploadDirectory = new File(uploadPath);
        if (!fileUploadDirectory.exists()) {
        	fileUploadDirectory.mkdirs();
        }
        try {
            List<FileItem> multiparts = new ServletFileUpload(
                                     new DiskFileItemFactory()).parseRequest(request);
          
            for(FileItem item : multiparts){
                if(!item.isFormField()){
                    String name = new File(item.getName()).getName();
                    item.write( new File(uploadDir + File.separator + name));
                }
            }
       
           //File uploaded successfully
           request.setAttribute("message", "File Uploaded Successfully");
        } catch (Exception ex) {
           request.setAttribute("message", "File Upload Failed due to " + ex);
        }    
 
        String errorString = null;
 
        // Product ID is the string literal [a-zA-Z_0-9]
        // with at least 1 character
        String regex = "\\w+";
 
        if (sender == null || !sender.matches(regex)) {
            errorString = "Sender is invalid!";
        }
        
        if (receiver == null || !receiver.matches(regex)) {
            errorString = "Receiver is invalid!";
        }
        
        if (paylod == null ) {
            errorString = "Message is invalid!";
        }
        
        if (contentType == null) {
        	contentType = "application/text";
        }
        
        
        String messageid = CommonUtil.createMessageId(sender, receiver);
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
        Message message = new Message(timeStamp, sender, receiver, messageid);
        
        Gateway gateway = new Gateway(properties);
        gateway.createMessage(message, paylod, contentType, null);
        // Then insert the data in DB
        if (errorString == null) {
            try {
            	
                DBops.insertMessage(conn, true, message);
            } catch (SQLException e) {
                e.printStackTrace();
                errorString = e.getMessage();
            }
        }
 
        // Store infomation to request attribute, before forward to views.
        request.setAttribute("errorString", errorString);
        request.setAttribute("message", message);
 
        // If error, forward to Edit page.
        if (errorString != null) {
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/send.jsp");
            dispatcher.forward(request, response);
        }
        // If everything nice.
        // Redirect to the monitoring page.
        else {
        	String recipientEndpoint = properties.getProperty("recipient.endpoint");
        	sendTheMessage(message, recipientEndpoint);
            response.sendRedirect(request.getContextPath() + "/monitoring");
        }
    }
    
    public static void sendTheMessage(Message message, String recipientEndpoint) throws IOException {
    	
    	HttpPost post = new HttpPost(recipientEndpoint);
    	post.setHeader("Content-type", message.getContentType());
    	post.setHeader("From", message.getSender());
    	post.setHeader("To", message.getReceiver());
    	post.setHeader("Date", message.getDate());
    	post.setHeader("Message-Id", message.getMessageid());
    	ByteArrayEntity byteArrayEntity = new ByteArrayEntity(message.getEncrypted());
    	post.setEntity(byteArrayEntity);
    	
    	try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
    		CloseableHttpResponse response = httpClient.execute(post);
    		System.out.println(response.getStatusLine());
    		System.out.println(response.getAllHeaders());
    	} catch (IOException e) {
    		message.setErrorString(e.toString());
    	}
    }
 
}