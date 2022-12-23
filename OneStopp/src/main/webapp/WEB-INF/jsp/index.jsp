<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
Welcome ${currentUser}
<form action= "/location" method = 'POST'>
${message}
<br>
<label>Location</label>
<input type="text" name = "location">
<input type="hidden" name = "currentUser" value = "${currentUser}">
<input type="hidden" name = "currentUserMail" value = "${currentUserMail}">
<input type="submit" name="submit" value="proceed">
</form>
<br>
</body>
