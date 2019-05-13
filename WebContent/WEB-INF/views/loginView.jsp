<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
		<title>Member Login</title>
	</head>

	<body>
		<div class="card text-white bg-info mx-auto mt-5" style="max-width: 18rem;">
			<h5  class="card-header">Member Login</h5>
			<div class="card-body">
				<form method="POST" action="${pageContext.request.contextPath}/login">
					<p style="color: red;">${errorString}</p>
					<div class="form-group">
						<label for="inputFirstName">User Name</label>
						<input type="text" class="form-control" name="userName" value="${user.userName}" required autofocus>
					</div>
					<div class="form-group">
						<label for="inputFirstName">Password</label>
						<input type="password" class="form-control" name="password" value="${user.password}" required>
					</div>
					<div class="form-group">
						<div >
                            <label>
                                <input type="checkbox" name="rememberMe" value="Y"> Remember me
                            </label>
                            <button type="submit" class="btn btn-primary float-right">Login</button>
                        </div>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>