<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
${message}
<form action = "/validateOTP" method = 'POST'>
<input type="password" name="otp">
<input type="hidden" name="firstName" value="${firstName}">
<input type="hidden" name="lastName" value="${lastName}">
<input type="hidden" name="email" value="${email}">
<input type="hidden" name="mobile" value="${mobile}">
<input type="hidden" name="password" value="${password}">
<input type="submit" name="submit" value="validate">
</form>
<form action ="/resendOTP" method='POST'>
<input type="hidden" name="firstName" value="${firstName}">
<input type="hidden" name="lastName" value="${lastName}">
<input type="hidden" name="email" value="${email}">
<input type="hidden" name="mobile" value="${mobile}">
<input type="hidden" name="password" value="${password}">
<input type="submit" name="submit" value="resend">
</form>
</body>
</html>