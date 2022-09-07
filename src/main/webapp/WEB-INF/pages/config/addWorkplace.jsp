<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <c:if test="${empty workplace.id}">
        <title>Добавление рабочего места</title>
    </c:if>
    <c:if test="${!empty workplace.id}">
        <title>Редактирование рабочего места</title>
    </c:if>

    <style>
        #error {
            background: rgba(102, 102, 102, 0.5);
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            display: none;
        }
        #window {
            width: 300px;
            height: 50px;
            text-align: center;
            padding: 15px;
            border: 3px solid #0000cc;
            border-radius: 10px;
            color: #0000cc;
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            margin: auto;
            background: #fff;
        }
        #error:target {display: block;}
        .close {
            display: inline-block;
            border: 1px solid #0000cc;
            color: #0000cc;
            padding: 0 12px;
            margin: 10px;
            text-decoration: none;
            background: #f2f2f2;
            font-size: 14pt;
            cursor:pointer;
        }
        .close:hover {background: #e6e6ff;}
    </style>

</head>
<body>
<c:if test="${empty workplace.id}">
    <c:url value="/config/add_workplace" var="var"/>
</c:if>
<c:if test="${!empty workplace.id}">
    <c:url value="/config/edit_workplace" var="var"/>
</c:if>
<form action="${var}" method="post">
    <c:if test="${!empty workplace.id}">
        <input type="hidden" name="id" value="${workplace.id}">
    </c:if>
    <label for="title">Название</label>
    <input type="text" name="title" id="title">
    <c:if test="${empty workplace.id}">
        <input type="submit" value="Добавить рабочее место">
    </c:if>
    <c:if test="${!empty workplace.id}">
        <input type="submit" value="Редактировать рабочее место">
    </c:if>
</form>

<!--<div id="error" class="modalbackground">
    <div class="modalwindow">
        <h3>Ошибка!</h3>
        <p>Проверьте введенные данные</p>
        <a href=" ">Закрыть</a>
    </div>
</div>-->
<c:out value="${error}"/>
<c:if test="${!empty error}">
    <div id="error">
        <div id="window">
            <p><c:out value="${error}"/> </p>
            <a href="#" class="close">Закрыть окно</a>
        </div>
    </div>
    <a href="#error">Вызвать всплывающее окно</a>
</c:if>




</body>
</html>