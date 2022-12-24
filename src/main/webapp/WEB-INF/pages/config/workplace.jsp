<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="workplaceManager.db.domain.Workplace" %>
<%@ page import="org.hibernate.jdbc.Work" %>
<%@ page import="workplaceManager.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Workplace workplace = (Workplace) request.getAttribute(Parameters.workplace);
        String redirect = (String) request.getAttribute(Parameters.redirect);
        String baseUrl = (String) request.getAttribute(Parameters.baseUrl);
        //String token = (String) request.getAttribute(Parameters.token);
        Role role = (Role) request.getSession().getAttribute(Parameters.role);

        String url = baseUrl;
        String buttonTitle = "";
        String title = "";
        if (workplace != null && workplace.getId() > 0) {
            title = "Редактирование рабочего места";
            url += Pages.updateWorkplacePost;
            buttonTitle = "Редактировать";
        } else {
            title = "Добавление рабочего места";
            url += Pages.addWorkplacePost;
            buttonTitle = "Добавить";
        }
    %>
    <title><%=title%>
    </title>
</head>

<body>
<section class="sticky">
    <%@include file='/WEB-INF/pages/header.jsp' %>
</section>
<h1 align="center"><%=title%>
</h1>
<%
    String error = (String) request.getAttribute(Parameters.error);
    if (error != null && !error.isEmpty()) {
%>
<h3><%=error%>
</h3>
<%
    }
    String message = (String) request.getAttribute(Parameters.message);
    if (message != null && !message.isEmpty()) {
%>
<h3><%=message%>
</h3>
<%
    }
%>

<form action="<%=url%>" method="post">
    <%
        if (workplace != null && workplace.getId() > 0) {
    %>
    <input type="hidden" name="id" value="<%=workplace.getId()%>">
    <%
        }
    %>
    <div class="wrapper_500" align="center">
        <p>
            <label for="title">Название</label>
            <input type="text" autofocus name="title" id="title" size="50"
                   value="<%=workplace!=null ? workplace.getTitleHtml() : ""%>">
        </p>
    </div>
    <div align="center">
        <p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" value="<%=buttonTitle%>">
            <%
                }
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <a href="<%=baseUrl + redirect%>" class="button">Назад</a>
        </p>
    </div>
</form>

</body>
</html>