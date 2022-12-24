<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="java.lang.reflect.Parameter" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Авторизация</title>
    <!--<link rel="icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">-->
    <link href="<c:url value="/css/styleLogin.css"/>" rel="stylesheet" type="text/css"/>
    <!--<link rel="stylesheet" href="/css/styleLogin.css" media="screen" type="text/css"/>-->
    <!--<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>-->
</head>

<body>

<div id="login-form">
    <h1>АВТОРИЗАЦИЯ</h1>
    <fieldset>
        <form action="<%=Pages.login%>" method="get" modelAttribute="userForm">
            <input type="text" name="username" id="username" required value="Логин"
                   onBlur="if(this.value=='')this.value='Логин'" onFocus="if(this.value=='Логин')this.value='' ">
            <input type="password" name="password" id="password" required value="Пароль"
                   onBlur="if(this.value=='')this.value='Пароль'" onFocus="if(this.value=='Пароль')this.value='' ">
            <%
                String error = (String) request.getAttribute(Parameters.error);
                if (error != null && !error.isEmpty()) {
            %>
            <p><%=error%>
            </p>
            <%
                }
            %>
            <input type="submit" value="ВОЙТИ">
            <p align="center"><a href="<%=Pages.registration%>">Зарегистрироваться</a></p>
        </form>
    </fieldset>
</div>
</body>
</html>