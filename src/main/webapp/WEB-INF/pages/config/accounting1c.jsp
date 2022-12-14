<%@ page import="workplaceManager.db.domain.Accounting1C" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Accounting1C accounting1C = (Accounting1C) request.getAttribute("accounting1c");
        String error = (String) request.getAttribute("error");
        String message = (String) request.getAttribute("message");
        String token = (String) request.getAttribute("token");

        String buttonTitle = "Добавить";
        String url = "";
        if (accounting1C != null && accounting1C.getId() > 0) {
    %>
    <title>Редактирование</title>
    <%
        url = "/" + Pages.updateAccounting1CPost;
        buttonTitle = "Редактировать";
    } else {
    %>
    <title>Добавление</title>
    <%
            url = "/" + Pages.addAccounting1CPost;
        }
    %>
</head>

<body>
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
            <input type="text" name="inventoryNumber" id="inventoryNumber" value="<%=inventoryNumber%>">
        </p>

        <p>
            <label for="title">Название</label>
            <%
                String title = (accounting1C != null && accounting1C.getTitle() != null) ? accounting1C.getTitle() : "";
            %>
            <input type="text" name="title" id="title" value="<%=title%>">
        </p>

        <p>
            <%
                Long employeeId = (accounting1C != null && accounting1C.getEmployee() != null) ? accounting1C.getEmployee().getId() : -1;
            %>
            <label for="employee_id">Материально-ответственное лицо</label>
            <select name="employee_id" id="employee_id">
                <option value="-1"/>
                <%
                    List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
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
                Role role = (Role) request.getAttribute("role");
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" value="<%=buttonTitle%>">
            <%
                }
                String redirect = (String) request.getAttribute("redirect");
            %>
            <input type="hidden" name="redirect" value="<%=redirect%>">
            <input type="hidden" name="token" value="<%=token%>">
            <a href="<%=redirect%>?token=<%=token%>" class="button">Назад</a>
        </p>
    </div>

</form>
</body>
</html>