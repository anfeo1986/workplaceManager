<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <tr><a href="/config/employee/addUpdateEmployee">Добавить сотрудника</a> </tr>
    <tr>
        <th>id</th>
        <th>ФИО</th>
        <th>Раб. место</th>
    </tr>
    <c:forEach var="employee" items="${employeeList}">
        <tr>
            <td>${employee.id}</td>
            <td>${employee.name}</td>
            <c:if test="${!empty employee.workplace}">
                <td>${employee.workplace.title}</td>
            </c:if>
            <c:if test="${empty employee.workplace}">
                <td/>
            </c:if>
            <td><a href="/config/employee/deleteEmployee?id=${employee.id}">Удалить</a> </td>
        </tr>
    </c:forEach>
</table>