<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: feoktistov
  Date: 26.08.2022
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <title>Title</title>
</head>
<body>
    <h2>Films</h2>
    <table>
        <tr>
            <th>id</th>
            <th>Название</th>
            <th>Год</th>
            <th>Жанр</th>
            <th>Просмотрено</th>
            <th>action</th>
        </tr>
        <c:forEach var="film" items="${filmsList}">
            <tr>
                <td>${film.id}</td>
                <td>${film.title}</td>
                <td>${film.year}</td>
                <td>${film.genre}</td>
                <td>${film.watched}</td>
                <td>
                    <a href="/edit/${film.id}">edit</a>
                    <a href="/delete/${film.id}">delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <h2>Add</h2>
    <c:url value="/add" var ="add"/>
    <a href="${add}">Add new film</a>
</body>
</html>
