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
${message}
<form action="/selectSlots" method='POST'>
<c:forEach items="${resList}" var="slots">
<input type="hidden" name = "location" value="${location}">
<input type="hidden" name = "currentUser" value = "${currentUser}">
<input type="hidden" name="currentUserMail" value="${currentUserMail}">
<input type="hidden" name="currentResId" value="${currentResId}">
<c:out value="${slots.date}"></c:out>
<br>
<c:out value="${slots.slot}"></c:out>
<input type="checkbox" name="currentSlot" value="${slots.date}/${slots.slot}">
<br>
</c:forEach>
<br>
<input type="submit" name="submit" value="select">
<!--<a href="/location/${restaurant.id}?location='${location}'&currentUser='${currentUser}'&currentUserMail='${currentUserMail}'"><c:out value="${restaurant.restaurantName}"></c:out></a>-->
</form>

</body>
</html>