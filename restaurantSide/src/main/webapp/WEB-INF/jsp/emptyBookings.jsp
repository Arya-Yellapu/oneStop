<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
${MESSAGE}
<br>
<c:forEach items="${restaurantList}" var="restaurant">
<a href="https://localhost:7071/getBookingsForRestaurant?restaurantId=${restaurant.id}&mail=${mail}">${restaurant.restaurantName}</a>
</c:forEach>
<a href="https://localhost:7071">Add More Slots</a>
</body>
</html>