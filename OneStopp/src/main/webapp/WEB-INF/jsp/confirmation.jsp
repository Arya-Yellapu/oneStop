<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
Your reservation is ${reservationid}
<br>
<form action= "/bookMore" method = 'POST'>
<input type="hidden" name = "currentUser" value = "${currentUser}">
<input type="hidden" name = "currentUserMail" value = "${currentUserMail}">
<input type="submit" name="submit" value="Book More Slots">
</form>
<br>
<a href="/logout">Logout</a>
<a href="https://localhost:${port}/getReservedSlots?currentUser=${currentUser}&currentUserMail=${currentUserMail}">Get My Bookings</a>
</body>
