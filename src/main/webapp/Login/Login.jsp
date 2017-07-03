<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../css/bootstrap.min.css">
<title>Please Identify Yourself</title>
<style>
	.row:nth-of-type(3) div {text-align: center; font-weight:bold}
	.row:nth-of-type(1) div, .row:nth-of-type(2) div, .row:nth-of-type(6) div {text-align: center}
	.login {margin: auto; width:350px}
	.row {
		margin-bottom: 10px
	}
</style>
<script>
	function disableLoginButton() {
		document.querySelectorAll('button')[0].disabled = true;
	}
</script>
</head>
<body>
	<form method="post" action="j_security_check" onSubmit="disableLoginButton();">
		<div class="container">
			<div class="login">
				<div class="row">
					<div class="col-sm-12"><img src="../img/key1.png"></div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<c:if test="${param.error != null}">
						    <div class="text-danger">
						        Invalid user name and password.
						    </div>
						</c:if>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">Identify Yourself</div>
				</div>
				<div class="row">
					<div class="col-sm-6">User Name:</div>
					<div class="col-sm-6"><input name="j_username"></div>
				</div>
				<div class="row">
					<div class="col-sm-6">Password:</div>
					<div class="col-sm-6"><input type="password" name="j_password"></div>
				</div>
				<div class="row">
					<div class="col-sm-12"><button>Login</button></div>
				</div>
			</div>
		</div>
	</form>
</body>
<script>
	document.querySelectorAll("input")[0].focus();
</script>
</html>

