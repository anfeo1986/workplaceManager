<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>

    <c:if test="${equipment.id > 0}">
        <title>Редактирование</title>
        <c:url var="url" value="/config/equipment/updateEquipmentPost"/>
        <c:set var="equipmentId" value="${equipment.id}"/>
        <c:set var="buttonTitle" value="Сохранить"/>
    </c:if>
    <c:if test="${equipment.id <= 0}">
        <title>Добавление</title>
        <c:url var="url" value="/config/equipment/addEquipmentPost"/>
        <c:set var="equipmentId" value="0"/>
        <c:set var="buttonTitle" value="Добавить"/>
    </c:if>

    <c:if test="${empty equipment.uid}">
        <c:set var="uid" value=""/>
    </c:if>
    <c:if test="${!empty equipment.uid}">
        <c:set var="uid" value="${equipment.uid}"/>
    </c:if>

    <c:if test="${empty equipment.manufacturer}">
        <c:set var="manufacturer" value=""/>
    </c:if>
    <c:if test="${!empty equipment.manufacturer}">
        <c:set var="manufacturer" value="${equipment.manufacturer}"/>
    </c:if>

    <c:if test="${empty equipment.model}">
        <c:set var="model" value=""/>
    </c:if>
    <c:if test="${!empty equipment.model}">
        <c:set var="model" value="${equipment.model}"/>
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
    <c:if test="${equipmentId > 0}">
        <input type="hidden" name="id" value="${equipmentId}">
    </c:if>

    <label for="uid">UID</label>
    <input type="text" name="uid" id="uid" value="${uid}">

    <label for="manufacturer">Производитель</label>
    <input type="text" name="manufacturer" id="manufacturer" value="${manufacturer}">

    <label for="model">Модель</label>
    <input type="text" name="model" id="model" value="${model}">

    <p>
        <input type="submit" value="${buttonTitle}">
        <input type="hidden" name="redirect" value="${redirect}">
        <input type="hidden" name="typeEquipment" value="${typeEquipment}">
        <a href="/${redirect}" class="button">Назад</a>
    </p>
</form>

</body>
</html>
