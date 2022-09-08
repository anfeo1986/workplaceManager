<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <c:if test="${workplace.id > 0}">
        <title>Редактирование рабочего места</title>
        <c:url value="/config/workplace/updateWorkplacePost" var="url"/>
        <c:set var="workplaceId" value="${workplace.id}"/>
        <c:set var="buttonTitle" value="Редактировать"/>
    </c:if>
    <c:if test="${workplace.id <= 0}">
        <title>Добавление рабочего места</title>
        <c:url value="/config/workplace/addWorkplacePost" var="url"/>
        <c:set var="workplaceId" value="0"/>
        <c:set var="buttonTitle" value="Добавить"/>
    </c:if>
    <c:if test="${empty workplace.title}">
        <c:set var="workplaceTitle" value=""/>
    </c:if>
    <c:if test="${!empty workplace.title}">
        <c:set var="workplaceTitle" value="${workplace.title}"/>
    </c:if>
</head>

<body>

<c:if test="${!empty error}">
    <h3><c:out value="${error}"/></h3>
</c:if>
<c:if test="${!empty message}">
    <h3><c:out value="${message}"/></h3>
</c:if>

<form action="${url}" method="post">
    <c:if test="${workplaceId > 0}">
        <input type="hidden" name="id" value="${workplaceId}">
    </c:if>

    <label for="title">Название</label>
    <input type="text" name="title" id="title" value="${workplaceTitle}">

    <p>
        <input type="submit" value="${buttonTitle}">
        <a href="/workplace" class="button">Назад</a>
    </p>
</form>

</body>
</html>