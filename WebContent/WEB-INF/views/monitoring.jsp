<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Monitoring</title>
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
            <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        </head>

        <body>

            <!-- NAVBAR -->
        <nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="${pageContext.request.contextPath}/home">iSMessage</a>
    </div>
    <ul class="nav navbar-nav">
      <li ><a href="${pageContext.request.contextPath}/home">Home</a></li>
      <li><a href="${pageContext.request.contextPath}/send">Send</a></li>
	  <li class="active"><a href="${pageContext.request.contextPath}/monitoring">Monitoring</a></li>
    </ul>
    <ul class="nav navbar-nav navbar-right">
       <li class="dropdown">
      	<a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-user"></span> User</a>
        <ul class="dropdown-menu">
          <li><a href="${pageContext.request.contextPath}/userInfo">My Profile</a></li>
          <li role="separator" class="divider"></li>
          <li><a href="${pageContext.request.contextPath}/login">Logout</a></li>
        </ul>
      </li>
    </ul>
  </div>
</nav>
        <!-- NAVBAR -->
			<div class="col-lg-8">
            <div class="panel panel-primary">
                <div class="panel-heading">Sent Messages</div>
                <div class="panel-body">
                    <p style="color: red;">${errorString}</p>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Sender</th>
                                <th>Receiver</th>
                                <th>Message Id</th>
                                <th>Signed Data</th>
                                <th>Encrypted Data</th>
                            </tr>
                        </thead>
                        <c:forEach items="${sentMessageList}" var="sentMessage">
                            <tbody>
                                <tr>
                                    <td>${sentMessage.date}</td>
                                    <td>${sentMessage.sender}</td>
                                    <td>${sentMessage.receiver}</td>
                                    <td>${sentMessage.messageid}</td>
                                    <td>
                                        <a href="viewTransaction?messagetype=sent&datatype=signed&messageid=${sentMessage.messageid}">View</a>
                                    </td>
                                    <td>
                                        <a href="viewTransaction?messagetype=sent&datatype=encrypted&messageid=${sentMessage.messageid}">View</a>
                                    </td>
                                </tr>
                            </tbody>
                        </c:forEach>
                    </table>
                </div>
            </div>
           </div>
<div class="col-lg-8">
            <div class="panel panel-primary">
                <div class="panel-heading">Received Messages</div>
                <div class="panel-body">
                    <p style="color: red;">${errorString}</p>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Sender</th>
                                <th>Receiver</th>
                                <th>Message Id</th>
                                <th>Signed Data</th>
                                <th>Encrypted Data</th>
                            </tr>
                        </thead>
                        <c:forEach items="${receivedMessageList}" var="receivedMessage">
                            <tbody>
                                <tr>
                                    <td>${receivedMessage.date}</td>
                                    <td>${receivedMessage.sender}</td>
                                    <td>${receivedMessage.receiver}</td>
                                    <td>${receivedMessage.messageid}</td>
                                    <td>
                                        <a href="viewTransaction?messagetype=received&datatype=signed&messageid=${receivedMessage.messageid}">View</a>
                                    </td>
                                    <td>
                                        <a href="viewTransaction?messagetype=received&datatype=encrypted&messageid=${receivedMessage.messageid}">View</a>
                                    </td>
                                </tr>
                            </tbody>
                        </c:forEach>
                    </table>
                </div>
            </div>
	</div>
            <jsp:include page="_footer.jsp"></jsp:include>

        </body>

        </html>