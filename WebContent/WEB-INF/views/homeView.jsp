<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Home Page</title>
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
      <li class="active"><a href="${pageContext.request.contextPath}/home">Home</a></li>
      <li><a href="${pageContext.request.contextPath}/send">Send</a></li>
	  <li><a href="${pageContext.request.contextPath}/monitoring">Monitoring</a></li>
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

        <h3>Home Page</h3> This application allows the user to send and receive the secure SMIME Message over HTTP.
        <br>
        <br>
        <b>It offers the following functions:</b>
        <ul>
            <li>Login</li>
            <li>Sending the Message</li>
            <li>Receiving the Message</li>
            <li>Storing in database</li>
            <li>Listing the Sent and received Message</li>
            <li>Viewing the User Information</li>
        </ul>

        <jsp:include page="_footer.jsp"></jsp:include>

    </body>

    </html>