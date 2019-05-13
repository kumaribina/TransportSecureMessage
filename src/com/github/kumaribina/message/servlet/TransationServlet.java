package com.github.kumaribina.message.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.github.kumaribina.message.db.DBUtils;
import com.github.kumaribina.message.db.DBops;
import com.github.kumaribina.message.dbo.Message;

@WebServlet(urlPatterns = { "/viewTransaction" })
public class TransationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	 
    public TransationServlet() {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = DBUtils.getStoredConnection(request);
 
        String messagetype = request.getParameter("messagetype");
        String datatype = request.getParameter("datatype");
        String messageid = request.getParameter("messageid");
        
        String errorString = null;
        Message message = null; 
        try {
        	message = DBops.findMessageByID(conn, messageid, messagetype.equals("sent"));
        	
        } catch (SQLException e) {
            e.printStackTrace();
            errorString = e.getMessage();
        }
        
        if (message != null) {
        	if (messagetype.equals("sent")) {
            	request.setAttribute("messageType", "Sent Message");
            } else {
            	request.setAttribute("messageType", "Received Message");
            }
            
            if (datatype.equals("signed")) {
            	request.setAttribute("dataType", "Signed");
            	request.setAttribute("data", IOUtils.toString(message.getSigned(), "UTF-8"));
            } else {
            	request.setAttribute("dataType", "Encrypted");
            	request.setAttribute("data", IOUtils.toString(message.getEncrypted(), "UTF-8"));
            }
            request.setAttribute("eachMessage", message);
        }
        
        request.setAttribute("errorString", errorString);
         
        // Forward to /WEB-INF/views/monitoring.jsp
        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/viewTransaction.jsp");
        dispatcher.forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
