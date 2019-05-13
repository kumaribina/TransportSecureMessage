package com.github.kumaribina.message.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.kumaribina.message.db.DBUtils;
import com.github.kumaribina.message.db.DBops;
import com.github.kumaribina.message.dbo.Message;

@WebServlet(urlPatterns = { "/monitoring" })
public class MonitoringServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public MonitoringServlet() {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = DBUtils.getStoredConnection(request);
 
        String errorString = null;
        List<Message> sentMessageList = null;
        List<Message> receivedMessageList = null;
        try {
        	sentMessageList = DBops.querySentMessage(conn, true);
        	receivedMessageList = DBops.querySentMessage(conn, false);
        } catch (SQLException e) {
            e.printStackTrace();
            errorString = e.getMessage();
        }
        // Store info in request attribute, before forward to views
        request.setAttribute("errorString", errorString);
        request.setAttribute("sentMessageList", sentMessageList);
        request.setAttribute("receivedMessageList", receivedMessageList);
         
        // Forward to /WEB-INF/views/monitoring.jsp
        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/monitoring.jsp");
        dispatcher.forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
 
}