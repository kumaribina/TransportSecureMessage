<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="UTF-8">
      <title>Send Message</title>
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
               <li class="active"><a href="${pageContext.request.contextPath}/send">Send</a></li>
               <li ><a href="${pageContext.request.contextPath}/monitoring">Monitoring</a></li>
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
      <div class="col-lg-6">
      <div class="panel panel-primary ">
         <div class="panel-heading">Send Secure Message</div>
         <div class="panel-body">
            <p style="color: red;">${errorString}</p>
            <form method="POST" action="${pageContext.request.contextPath}/send">
               <div class="form-group row">
                  <label for="sender" class="col-sm-2 col-form-label">Sender</label>
                  <div class="col-sm-10">
                     <input type="text" class="form-control" id="sender" aria-describedby="senderHelp" placeholder="Enter Sender" name="sender" value="${message.sender}">
                     <small id="senderHelp" class="form-text text-muted">Enter your id which will act as a sender id, e.g. Tom.</small>
                  </div>
               </div>
               <div class="form-group row">
                  <label for="receiver" class="col-sm-2 col-form-label">Receiver</label>
                  <div class="col-sm-10">
                     <input type="text" class="form-control" id="receiver" aria-describedby="receiverHelp" placeholder="Enter Receiver" name="receiver" value="${message.receiver}">
                     <small id="receiverHelp" class="form-text text-muted">Enter your recipient id which will act as a receiver id, e.g. Jerry.</small>
                  </div>
               </div>
               <div class="form-group row">
                  <label for="messageArea" class="col-sm-2 col-form-label">Message</label>
                  <div class="col-sm-10">
                     <textarea class="form-control" id="messageArea" rows="3" name="paylod" placeholder="Write something.." aria-describedby="messageHelp"></textarea>
                     <small id="messageHelp" class="form-text text-muted">Enter the message you want to deliver.</small>
                  </div>
               </div>
               <div class="form-group row">
                  <label for="messageArea" class="col-sm-2 col-form-label">Content Type</label>
                  <div class="col-sm-10">
                     <input type="text" name="contentType" list="contentType" aria-describedby="contentTypeHelp"/>
					 <datalist id="contentType">
					    <option value="application/xml">application/xml</option>
					    <option value="application/json">application/json</option>
					    <option value="text/plain">text/plain</option>
					 </datalist>
					 <small id="contentTypeHelp" class="form-text text-muted">Enter the message type.</small>
                  </div>
               </div>
               <div class="form-group row">
                  <label for="attachFile" class="col-sm-2 col-form-label">Attach File</label>
                  <div class="col-sm-10">
                     <input type="file" class="form-control-file" id="attachFile" name="attachFile" aria-describedby="attachHelp">
                     
                  </div>
               </div>
               <button type="submit" class="btn btn-primary">Submit</button>
               <a href="monitoring">Cancel</a>
            </form>
         </div>
      </div>
     </div>
      <jsp:include page="_footer.jsp"></jsp:include>
   </body>
</html>