<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <c:if test="${employee.id > 0}">
        <title>Редактирование сотрудника</title>
        <c:url value="/config/employee/updateEmployeePost" var="url"/>
        <c:set var="employee_id" value="${employee.id}"/>
        <c:set var="buttonTitle" value="Редактировать"/>
    </c:if>
    <c:if test="${employee.id <= 0}">
        <title>Добавление сотрудника</title>
        <c:url value="/config/employee/addEmployeePost" var="url"/>
        <c:set var="employee_id" value="0"/>
        <c:set var="buttonTitle" value="Добавить"/>
    </c:if>

    <c:if test="${empty employee.name}">
        <c:set var="employeeName" value=""/>
    </c:if>
    <c:if test="${!empty employee.name}">
        <c:set var="employeeName" value="${employee.name}"/>
    </c:if>

    <c:if test="${empty employee.post}">
        <c:set var="employeePost" value=""/>
    </c:if>
    <c:if test="${!empty employee.post}">
        <c:set var="employeePost" value="${employee.post}"/>
    </c:if>

    <c:if test="${empty employee.workplace}">
        <c:set var="employeeWorkplaceId" value=""/>
    </c:if>
    <c:if test="${!empty employee.workplace}">
        <c:set var="employeeWorkplaceId" value="${employee.workplace.id}"/>
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
    <c:if test="${employee_id > 0}">
        <input type="hidden" name="id" value="${employee_id}">
    </c:if>

    <label for="name">ФИО</label>
    <input type="text" name="name" id="name" value="${employeeName}">

    <label for="post">Должность</label>
    <input type="text" name="post" id="post" value="${employeePost}">

    <label>Рабочее место</label>
    <select name="workplace_id">
        <option value="-1"/>
        <%--<option disabled>Выберите рабочее место</option>--%>
        <c:forEach var="workplace" items="${workplaceList}">
            <c:if test="${employeeWorkplaceId == workplace.id}">
                <option selected value="${workplace.id}" >${workplace.title}</option>
            </c:if>
            <c:if test="${employeeWorkplaceId != workplace.id}">
                <option value="${workplace.id}">${workplace.title}</option>
            </c:if>
        </c:forEach>
    </select>

    <p>
        <input type="submit" value="${buttonTitle}">
        <input type="hidden" name="redirect" value="${redirect}">
        <a href="/${redirect}" class="button">Назад</a>
    </p>
</form>

</body>
</html>
