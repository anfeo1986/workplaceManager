<%@ page import="workplaceManager.Pages" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Log in with your account</title>
</head>

<body>
<sec:authorize access="isAuthenticated()">
    <% response.sendRedirect("/"); %>
</sec:authorize>
<div>
    <form:form method="GET" action="<%=Pages.login%>" modelAttribute="userForm">
    <!--<form method="GET" action="/login">-->
        <h2>Вход в систему</h2>
        <div>
            <input name="username" id="username" type="text" placeholder="Username"
                   autofocus="true"/>
            <input name="password" id="password" type="password" placeholder="Password"/>
            <button type="submit">Войти</button>
            <h4><a href="<%=Pages.registration%>">Зарегистрироваться</a></h4>
        </div>
    </form:form>
    <!--</form>-->
</div>

</body>
</html>