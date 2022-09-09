<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Рабочие места</h1>
<a href="/config/workplace/addUpdateWorkplace">Добавить рабочее место</a>

<table>
    <tr>
        <th>id</th>
        <th>Название</th>
        <th><a href="/employee">Сотрудники</a></th>
    </tr>
    <c:forEach var="workplace" items="${workplaceList}">
        <tr>
            <td>${workplace.id}</td>
            <td><a href="/config/workplace/addUpdateWorkplace?id=${workplace.id}">${workplace.title}</a></td>
            <td>
                <c:if test="${!empty workplace.employeeList}">
                    <c:forEach var="employee" items="${workplace.employeeList}">
                        <p><a href="/config/employee/addUpdateEmployee?id=${employee.id}&redirect=workplace">${employee.name}</a></p>
                    </c:forEach>
                </c:if>
            </td>
            <td><a href="/config/workplace/deleteWorkplace?id=${workplace.id}">Удалить</a> </td>
        </tr>
    </c:forEach>

</table>