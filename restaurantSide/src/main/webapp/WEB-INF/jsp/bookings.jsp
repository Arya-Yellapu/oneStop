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
<a>Mail</a>
<a>Date</a>
<a>Time Slot</a>
<a>Slots Number</a>
<a>Max Number</a>
<br>
<c:forEach items="${finalList}" var="finalSlots">
<c:out value="${finalSlots.reservationId}"></c:out>
<c:out value="${finalSlots.slotsstatus}"></c:out>
<c:out value="${finalSlots.mailId}"></c:out>
<c:out value="${finalSlots.date_field}"></c:out>
<c:out value="${finalSlots.slot}"></c:out>
<c:out value="${finalSlots.slotsnumber}"></c:out>
<c:out value="${finalSlots.maxnumber}"></c:out>
<br>
</c:forEach>
<a href="https://localhost:7071">Add More Slots</a>
</body>
</html>