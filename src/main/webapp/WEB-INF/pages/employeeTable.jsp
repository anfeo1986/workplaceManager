<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Сотрудники</h1>
<a href="/config/employee/addUpdateEmployee?redirect=employee">Добавить сотрудника</a>

<table>
    <tr>
        <th>id</th>
        <th>ФИО</th>
        <th>Должность</th>
        <th><a href="/workplace">Рабочее место</a></th>
    </tr>
    <c:forEach var="employee" items="${employeeList}">
        <tr>
            <td>${employee.id}</td>

            <td><a href="/config/employee/addUpdateEmployee?id=${employee.id}&redirect=employee">${employee.name}</a></td>

            <td>${employee.post}</td>

            <c:if test="${!empty employee.workplace}">
                <td><a href="/config/workplace/addUpdateWorkplace?id=${employee.workplace.id}&redirect=employee">${employee.workplace.title}</a></td>
            </c:if>
            <c:if test="${empty employee.workplace}">
                <td/>
            </c:if>
            <td><a href="/config/employee/deleteEmployee?id=${employee.id}">Удалить</a> </td>
        </tr>
    </c:forEach>
</table>