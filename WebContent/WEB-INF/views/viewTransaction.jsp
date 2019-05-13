<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Transaction</title>
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
      <div class="panel panel-primary ">
         <div class="panel-heading">${messageType}</div>
         <div class="panel-body">
            <p style="color: red;">${errorString}</p>
				<div class="form-group row">
                  <label for="sender" class="col-sm-2 col-form-label">Date</label>
                  <div class="col-sm-10">
                     ${eachMessage.date}
                  </div>
               </div>
               <div class="form-group row">
                  <label for="sender" class="col-sm-2 col-form-label">Sender</label>
                  <div class="col-sm-10">
                     ${eachMessage.sender}
                  </div>
               </div>
               <div class="form-group row">
                  <label for="receiver" class="col-sm-2 col-form-label">Receiver</label>
                  <div class="col-sm-10">
                     ${eachMessage.receiver}
                  </div>
               </div>
               <div class="form-group row">
                  <label for="messageArea" class="col-sm-2 col-form-label">Message ID</label>
                  <div class="col-sm-10">
                     ${eachMessage.messageid}
                  </div>
               </div>
			   <div class="form-group row">
                  <label for="messageArea" class="col-sm-2 col-form-label">Message Type</label>
                  <div class="col-sm-10">
                     ${dataType}
                  </div>
               </div>
               <div class="form-group row">
                  <label for="attachFile" class="col-sm-2 col-form-label">Message</label>
                  
                  <div class="col-sm-10">
                  <textarea id="messageArea" name="paylod"> ${data}</textarea>
                    
                  </div>
               </div>
         </div>
      </div>
     </div>
            <jsp:include page="_footer.jsp"></jsp:include>

        </body>

        </html>