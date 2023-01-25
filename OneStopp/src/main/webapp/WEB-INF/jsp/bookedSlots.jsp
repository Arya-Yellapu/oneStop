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
<a>Reservation Id</a>
<a>Status</a>
<a>Restaurant Name</a>
<a>Location</a>
<a>Date</a>
<a>Time Slot</a>
<a>Max Pax</a>
<br>
<c:forEach items="${finalSlots}" var="finalSlot">
<c:out value="${finalSlot.reservationId}"></c:out>
<c:out value="${finalSlot.slotsstatus}"></c:out>
<c:out value="${finalSlot.restaurantName}"></c:out>
<c:out value="${finalSlot.location}"></c:out>
<c:out value="${finalSlot.date_field}"></c:out>
<c:out value="${finalSlot.slot}"></c:out>
<c:out value="${finalSlot.maxnumber}"></c:out>
<form action="/cancelSlot" method='POST'>
<input type="hidden" name="reservationId" value="${finalSlot.reservationId}">
<input type="hidden" name="slotId" value="${finalSlot.slotId}">
<input type="hidden" name = "currentUser" value = "${currentUser}">
<input type="hidden" name = "currentUserMail" value = "${currentUserMail}">
<input type="submit" name="Submit" value="Cancel">
</form>
<br>
</c:forEach>
<a href="https://localhost:${port}">Book A New Slot</a>
<a href="/logout">Logout</a>
</body>
</html>