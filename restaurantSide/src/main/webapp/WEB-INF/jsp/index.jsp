<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
Welcome ${name}
<form action="/addSlots" method='POST'>
<input type="hidden" name="mail" value="${mail}">
<input type="submit" name="submit" value="Add Restaurant Slots">
</form>
<a href="https://localhost:7071/viewRestaurants?mail=${mail}">Get My Restaurants</a>
</body>
</html>