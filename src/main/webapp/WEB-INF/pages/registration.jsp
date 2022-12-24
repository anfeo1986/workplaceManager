<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <!--<link rel="icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLogin.css" type="text/css" />
    <!--<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>-->
</head>

<body>
<%
    String error = (String) request.getAttribute(Parameters.passwordError);
%>

<div id="login-form">
    <h1>РЕГИСТРАЦИЯ</h1>
    <fieldset>
        <form action="<%=Pages.registration%>" method="post" modelAttribute="userForm">
            <input type="text" name="username" id="username" required value="Логин" onBlur="if(this.value=='')this.value='Логин'" onFocus="if(this.value=='Логин')this.value='' ">
            <input type="password" name="password" id="password" required value="Пароль" onBlur="if(this.value=='')this.value='Пароль'" onFocus="if(this.value=='Пароль')this.value='' ">
            <input type="password" name="passwordConfirm" id="passwordConfirm" required value="Пароль" onBlur="if(this.value=='')this.value='Пароль'" onFocus="if(this.value=='Пароль')this.value='' ">
            <%
                if(error != null && !error.isEmpty()) {
            %>
            <p><%=error%></p>
            <%
                }
            %>
            <input type="submit" value="Регистрация">
        </form>
    </fieldset>
</div>
</body>
</html>