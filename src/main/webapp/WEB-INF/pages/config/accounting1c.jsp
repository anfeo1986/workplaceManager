<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <c:if test="${accounting1c.id > 0}">
        <title>Редактирование</title>
        <c:url var="url" value="/config/accounting1c/updateAccounting1C"/>
        <c:set var="accounting1cId" value="${accounting1c.id}"/>
        <c:set var="buttonTitle" value="Сохранить"/>
    </c:if>
    <c:if test="${accounting1c.id <= 0}">
        <title>Добавление</title>
        <c:url var="url" value="/config/accounting1c/addAccounting1CPost"/>
        <c:set var="accounting1cId" value="0"/>
        <c:set var="buttonTitle" value="Добавить"/>
    </c:if>

    <c:if test="${!empty accounting1c.inventoryNumber}">
        <c:set var="inventoryNumber" value="${accounting1c.inventoryNumber}"/>
    </c:if>
    <c:if test="${empty accounting1c.inventoryNumber}">
        <c:set var="inventoryNumber" value=""/>
    </c:if>

    <c:if test="${!empty accounting1c.title}">
        <c:set var="title" value="${accounting1c.title}"/>
    </c:if>
    <c:if test="${empty accounting1c.title}">
        <c:set var="title" value=""/>
    </c:if>

    <c:if test="${!empty accounting1c.employee}">
        <c:set var="employeeId" value="${accounting1c.employee.id}"/>
    </c:if>
    <c:if test="${empty accounting1c.employee}">
        <c:set var="employeeId" value=""/>
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
    <c:if test="${accounting1cId > 0}">
        <input type="hidden" name="id" value="${accounting1cId}">
    </c:if>
    <div class="wrapper_500">
        <p>
            <label for="inventoryNumber">Инвентарный номер</label>
            <input type="text" name="inventoryNumber" id="inventoryNumber" value="${inventoryNumber}">
        </p>

        <p>
            <label for="title">Название</label>
            <input type="text" name="title" id="title" value="${title}">
        </p>

        <p>
            <label for="employee_id">Материально-ответственное лицо</label>
            <select name="employee_id" id="employee_id">
                <option value="-1"/>
                <c:forEach var="employee" items="${employeeList}">
                    <c:if test="${employeeId == employee.id}">
                        <option selected value="${employee.id}">${employee.name}</option>
                    </c:if>
                    <c:if test="${employeeId != employee.id}">
                        <option value="${employee.id}">${employee.name}</option>
                    </c:if>
                </c:forEach>
            </select>
        </p>
    </div>

    <div align="center">
        <p>
            <input type="submit" value="${buttonTitle}">
            <input type="hidden" name="redirect" value="${redirect}">
            <a href="/${redirect}" class="button">Назад</a>
        </p>
    </div>

</form>

</body>
</html>