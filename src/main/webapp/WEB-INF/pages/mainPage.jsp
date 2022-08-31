<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: administrator
  Date: 30.08.2022
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <title>Title</title>
</head>
<body>
<table>
    <tr>
        <td>
            <form action="/add_employee" method="post">
                <label for="name">ФИО</label>
                <input type="text" name="name" id="name">
                <select name="workplace">
                    <option disabled>Выберите рабочее место</option>
                    <c:forEach var="workplace" items="${workplaceList}">
                        <option value="${workplace}">${workplace.title}</option>
                    </c:forEach>
                </select>
                <input type="submit" value="добавить сотрудника">
            </form>
        </td>
        <td>
            <form action="/add_workplace" method="post">
                <label for="title">Название</label>
                <input type="text" name="title" id="title">
                <input type="submit" value="добавить рабочее место">
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <table>
                <tr>
                    <th>id</th>
                    <th>ФИО</th>
                </tr>
                <c:forEach var="employee" items="${employeeList}">
                    <tr>
                        <td>${employee.id}</td>
                        <td>${employee.name}</td>
                        <td><a href="/delete_employee/?id=${employee.id}">Удалить</a> </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
        <td>
            <table>
                <tr>
                    <th>id</th>
                    <th>Название</th>
                </tr>
                <c:forEach var="workplace" items="${workplaceList}">
                    <tr>
                        <td>${workplace.id}</td>
                        <td>${workplace.title}</td>
                        <td><a href="/delete_workplace/?id=${workplace.id}">Удалить</a> </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
</table>

</body>
</html>
