<%@ page import="workplaceManager.db.domain.Accounting1C" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Accounting1C accounting1C = request.getAttribute(Parameters.accounting1C) != null ?
                (Accounting1C) request.getAttribute(Parameters.accounting1C) : null;
        String error = request.getAttribute(Parameters.error) != null ?
                (String) request.getAttribute(Parameters.error) : "";
        String message = request.getAttribute(Parameters.message) != null ?
                (String) request.getAttribute(Parameters.message) : "";
        String baseUrl = request.getAttribute(Parameters.baseUrl) != null ?
                (String) request.getAttribute(Parameters.baseUrl) : "";
        Role role = request.getSession().getAttribute(Parameters.role) != null ?
                (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;
        Boolean isClose = request.getAttribute(Parameters.closeWindow) != null ?
                (Boolean) request.getAttribute(Parameters.closeWindow) : false;
        List<Employee> employeeList = request.getAttribute(Parameters.employeeList) != null ?
                (List<Employee>) request.getAttribute(Parameters.employeeList) : new ArrayList<>();
        String redirect = (String) request.getAttribute(Parameters.redirect);

        String buttonTitle = "Добавить";
        String url = baseUrl;
        String titlePage = "";
        if (accounting1C != null && accounting1C.getId() > 0) {
            titlePage = "Добавление записи в бухгалтерии";
            url += Pages.updateAccounting1CPost;
            buttonTitle = "Редактировать";
        } else {
            titlePage = "Редактирование записи в бухгалтерии";
            url += Pages.addAccounting1CPost;
        }
    %>
    <title><%=titlePage%>
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
<h1 align="center"><%=titlePage%>
</h1>
<%
    if (error != null && error != "") {
%>
<h3><%=error%>
</h3>
<%
    }
    if (message != null && message != "") {
%>
<h3><%=message%>
</h3>
<%
    }
%>
<form action="<%=url%>" method="post">
    <%
        if (accounting1C != null && accounting1C.getId() > 0) {
    %>
    <input type="hidden" name="id" value="<%=accounting1C.getId()%>">
    <%
        }
    %>
    <div class="wrapper_500">

        <p>
            <label for="inventoryNumber">Инвентарный номер</label>
            <%
                String inventoryNumber = (accounting1C != null && accounting1C.getInventoryNumber() != null)
                        ? accounting1C.getInventoryNumber() : "";
            %>
            <input type="text" name="inventoryNumber" id="inventoryNumber" autofocus value="<%=inventoryNumber%>">
        </p>

        <p>
            <label for="title">Название</label>
            <%
                String title = (accounting1C != null && accounting1C.getTitle() != null) ?
                        accounting1C.getTitleHtml() : "";
            %>
            <input type="text" name="title" id="title" size="50" value="<%=title%>">
        </p>

        <p>
            <%
                Long employeeId = (accounting1C != null && accounting1C.getEmployee() != null) ?
                        accounting1C.getEmployee().getId() : -1;
            %>
            <label for="<%=Parameters.employeeId%>">Материально-ответственное лицо</label>
            <select name="<%=Parameters.employeeId%>" id="<%=Parameters.employeeId%>">
                <option value="-1"/>
                <%
                    for (Employee employee : employeeList) {
                        if (employee.getId() == employeeId) {
                %>
                <option selected value="<%=employee.getId()%>"><%=employee.getName()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=employee.getId()%>"><%=employee.getName()%>
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
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <a onclick="close_window(); return false;" class="button">Назад</a>
                    <%
            if (Role.ADMIN.equals(role) && accounting1C != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.deleteAccounting1CPost%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>">
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