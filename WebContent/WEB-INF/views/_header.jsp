<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
  
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="${pageContext.request.contextPath}/home">WebSiteName</a>
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