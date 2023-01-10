<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.*" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        /* Employee employee = request.getAttribute(Parameters.employee) != null ?
                 (Employee) request.getAttribute(Parameters.employee) : null;*/
        String buttonTitle = "Сохранить";
        MainSettings mainSettings = request.getAttribute(Parameters.mainSettings) != null ?
                (MainSettings) request.getAttribute(Parameters.mainSettings) : new MainSettings();
        String baseUrl = request.getAttribute(Parameters.baseUrl) != null ?
                (String) request.getAttribute(Parameters.baseUrl) : "";
        Role role = request.getSession().getAttribute(Parameters.role) != null ?
                (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;
        /*Long workplaceIdFromRequest = request.getParameter(Parameters.workplaceId) != null ?
                Long.parseLong(request.getParameter(Parameters.workplaceId)) : -1L;*/
        Boolean isClose = request.getAttribute(Parameters.closeWindow) != null ?
                (Boolean) request.getAttribute(Parameters.closeWindow) : false;
        /*List<Workplace> workplaceList = request.getAttribute(Parameters.workplaceList) != null ?
                (List<Workplace>) request.getAttribute(Parameters.workplaceList) : new ArrayList<>();*/


        String url = baseUrl + Pages.mainSettingsSavePost;
        String title = "Настройка системы";
        /*if (employee != null && employee.getId() > 0) {
            title = "Редактирование материально-отвественного лица";
            url += Pages.updateEmployeePost;
            buttonTitle = "Редактировать";
        } else {
            title = "Добавление материально-отвественного лица";
            url += Pages.addEmployeePost;
        }*/
    %>
    <title><%=title%>
    </title>

    <script type="text/javascript">
        function close_window() {
            close();
        }
    </script>
</head>

<body>
<%
    if (isClose) {
%>
<script>
    close_window();
</script>
<%
    }
%>
<section class="sticky">
    <%@include file='/WEB-INF/pages/header.jsp' %>
</section>
<h1 align="center"><%=title%>
</h1>
<%
    String error = (String) request.getAttribute(Parameters.error);
    if (error != null && error != "") {
%>
<h3><%=error%>
</h3>
<%
    }
    String message = (String) request.getAttribute(Parameters.message);
    if (message != null && message != "") {
%>
<h3><%=message%>
</h3>
<%
    }
%>
<form action="<%=url%>" method="post">
    <div class="wrapper_1100">
        <p class="align_p">
            <label for="<%=Parameters.mainSettingsPrivateKey%>">Приватный ключ</label>
            <textarea name="<%=Parameters.mainSettingsPrivateKey%>" id="<%=Parameters.mainSettingsPrivateKey%>"
                      rows="5" cols="120"><%=(mainSettings != null && mainSettings.getPrivateKey() != null) ? mainSettings.getPrivateKey() : ""%></textarea>
        </p>

        <p class="align_p">
            <label for="<%=Parameters.mainSettingsPublicKey%>">Публичный ключ</label>
            <textarea name="<%=Parameters.mainSettingsPublicKey%>" id="<%=Parameters.mainSettingsPublicKey%>"
                      rows="5" cols="120"><%=(mainSettings != null && mainSettings.getPublicKey() != null) ? mainSettings.getPublicKey() : ""%></textarea>
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
            <a onclick="close_window(); return false;" class="button">Назад</a>
        </p>
    </div>
</form>
</body>
</html>
