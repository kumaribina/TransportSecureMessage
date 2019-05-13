package com.github.kumaribina.message.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.github.kumaribina.message.Gateway;
import com.github.kumaribina.message.db.DBUtils;
import com.github.kumaribina.message.db.DBops;
import com.github.kumaribina.message.dbo.Message;
import com.github.kumaribina.message.util.CommonUtil;

@WebServlet(urlPatterns = { "/http/receive" })
public class ReceiveServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public ReceiveServlet() {
        super();
    }
 
    // Show product creation page.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println(request.getRemoteAddr());
    	System.out.println(request.getHeader("From"));
    	doPost(request, response);
    }
 

    // This method will be called.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = DBUtils.getStoredConnection(request);
 
        String sender = request.getHeader("From");
        String receiver = request.getHeader("To");
        String messageid = request.getHeader("Message-Id");
        String contentType = request.getHeader("Content-Type");
        String date = request.getHeader("Date");
        InputStream stream = request.getInputStream();
       
 
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
        
        Properties properties = CommonUtil.getProperties(this.getServletContext());
        Message message = new Message(date, sender, receiver, messageid);
        message.setEncrypted(IOUtils.toByteArray(stream));
        message.setContentType(contentType);
        Gateway gateway = new Gateway(properties);
        gateway.processMessage(message);
        // Then insert the data in DB
        if (errorString == null) {
            try {
            	
                DBops.insertMessage(conn, false, message);
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
                    .getRequestDispatcher("/WEB-INF/views/monitoring.jsp");
            dispatcher.forward(request, response);
        }
        // If everything nice.
        // Redirect to the monitoring page.
        else {
            response.sendRedirect(request.getContextPath() + "/monitoring");
        }
    }
 
}