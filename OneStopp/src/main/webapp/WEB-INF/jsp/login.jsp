<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<label>Login</label>
${message}
<form action="/login" method='POST'>
${SPRING_SECURITY_LAST_EXCEPTION.message}
<br>
<label>Email</label>
<input type="text" name="email">
<br>
<label>Password</label>
<input type="password" name ="password">
<br>
<input type="submit" name = "submit">
</form>
<a href="/oauth2/authorization/google">Sign in with Google</a>
<br>
<a href="/register">SignUp</a>
</body>
</htm