<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="/saveRegistrationDetails" method='POST'>
${MESSAGE}
<br>
<label>First Name</label>
<input type="text" name="firstName">
<br>
<label>Last Name</label>
<input type="text" name="lastName">
<br>
<label>Mobile Number</label>
<input type="text" name="mobileNumber">
<br>
<label>Email</label>
<input type="text" name="email">
<br>
<label>Password</label>
<input type="password" name ="password">
<br>
<input type="submit" name = "submit">
</form>
</body>
</html>