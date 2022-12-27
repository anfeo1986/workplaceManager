<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="workplaceManager.db.domain.Workplace" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Employee employee = request.getAttribute(Parameters.employee) != null ?
                (Employee) request.getAttribute(Parameters.employee) : null;
        String buttonTitle = "Добавить";
        String baseUrl = request.getAttribute(Parameters.baseUrl) != null ?
                (String) request.getAttribute(Parameters.baseUrl) : "";
        Role role = request.getSession().getAttribute(Parameters.role) != null ?
                (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;
        Long workplaceIdFromRequest = request.getParameter(Parameters.workplaceId) != null ?
                Long.parseLong(request.getParameter(Parameters.workplaceId)) : -1L;
        Boolean isClose = request.getAttribute(Parameters.closeWindow) != null ?
                (Boolean) request.getAttribute(Parameters.closeWindow) : false;
        List<Workplace> workplaceList = request.getAttribute(Parameters.workplaceList) != null ?
                (List<Workplace>) request.getAttribute(Parameters.workplaceList) : new ArrayList<>();


    String url = baseUrl;
        String title = "";
        if (employee != null && employee.getId() > 0) {
            title = "Редактирование материально-отвественного лица";
            url += Pages.updateEmployeePost;
            buttonTitle = "Редактировать";
        } else {
            title = "Добавление материально-отвественного лица";
            url += Pages.addEmployeePost;
        }
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
    <%
        if (employee != null && employee.getId() > 0) {
    %>
    <input type="hidden" name="id" value="<%=employee.getId()%>">
    <%
        }
    %>
    <div class="wrapper_500">
        <p>
            <label for="name">ФИО</label>
            <%
                String employeeName = (employee != null && employee.getName() != null) ?
                        employee.getName() : "";
            %>

            <input type="text" name="name" id="name" autofocus size="56" value="<%=employeeName%>">
        </p>

        <p>
            <label for="post">Должность</label>
            <%
                String employeePost = (employee != null && employee.getPost() != null) ?
                        employee.getPost() : "";
            %>
            <input type="text" name="post" id="post" size="50" value="<%=employeePost%>">
        </p>

        <p>
            <label>Рабочее место</label>
            <select name="<%=Parameters.workplaceId%>">
                <option value="-1"/>
                <%
                    Long workplaceId = (employee != null && employee.getWorkplace() != null) ?
                            employee.getWorkplace().getId() : -1;
                    for (Workplace workplace : workplaceList) {
                        if (workplace.getId() == workplaceId || workplace.getId() == workplaceIdFromRequest) {
                %>
                <option selected value="<%=workplace.getId()%>"><%=workplace.getTitleHtml()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=workplace.getId()%>">
                    <%=workplace.getTitleHtml()%>
                </option>
                <%
                        }
                    }
                %>
            </select>
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
                String redirect = (String) request.getAttribute(Parameters.redirect);
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <a onclick="close_window(); return false;" class="button">Назад</a>
                <%
            if (Role.ADMIN.equals(role) && employee != null) {
        %>
        <td>
            <a href="<%=baseUrl+Pages.deleteEmployeePost%>?<%=Parameters.id%>=<%=employee.getId()%>">
                Удалить
            </a>
        </td>
        <%
            }
        %>
        </p>
    </div>
</form>
</body>
</html>
