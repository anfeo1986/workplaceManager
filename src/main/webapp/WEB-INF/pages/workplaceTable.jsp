<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <tr><a href="/config/workplace/addUpdateWorkplace">Добавить рабочее место</a> </tr>
    <tr>
        <th>id</th>
        <th>Название</th>
    </tr>
    <c:forEach var="workplace" items="${workplaceList}">
        <tr>
            <td>${workplace.id}</td>
            <td><a href="/config/workplace/addUpdateWorkplace?id=${workplace.id}">${workplace.title}</a></td>
            <td><a href="/config/workplace/deleteWorkplace?id=${workplace.id}">Удалить</a> </td>
        </tr>
    </c:forEach>

</table>