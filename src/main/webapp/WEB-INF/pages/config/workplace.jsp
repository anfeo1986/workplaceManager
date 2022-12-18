<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="workplaceManager.db.domain.Workplace" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page import="org.hibernate.jdbc.Work" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Workplace workplace = (Workplace) request.getAttribute(Parameters.workplace);
        String redirect = (String) request.getAttribute(Parameters.redirect);
        String baseUrl = (String) request.getAttribute(Parameters.baseUrl);
        String token = (String) request.getAttribute(Parameters.token);
        String url = baseUrl;
        String buttonTitle = "";
        if (workplace != null && workplace.getId() > 0) {
    %>
    <title>Редактирование рабочего места</title>
    <%
        url += Pages.updateWorkplacePost;
        buttonTitle = "Редактировать";
    } else {
    %>
    <title>Добавление рабочего места</title>
    <%
            url += Pages.addWorkplacePost;
            buttonTitle = "Добавить";
        }
    %>
</head>

<body>
<section class="sticky">
    <%@include file='/WEB-INF/pages/header.jsp' %>
</section>
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
            <input type="text" name="title" id="title" value="<%=workplace!=null ? workplace.getTitle() : ""%>">
        </p>
    </div>
    <div align="center">
        <p>
            <%
                Role role = (Role) request.getAttribute(Parameters.role);
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" value="<%=buttonTitle%>">
            <%
                }
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <input type="hidden" name="<%=Parameters.token%>" value="<%=token%>">
            <a href="<%=baseUrl + redirect%>?<%=Parameters.token%>=<%=token%>" class="button">Назад</a>
        </p>
    </div>
</form>

</body>
</html>