<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.db.domain.Workplace" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Employee employee = (Employee) request.getAttribute(Parameters.employee);
        String buttonTitle = "Добавить";
        String token = (String) request.getAttribute(Parameters.token);
        String baseUrl = (String) request.getAttribute(Parameters.baseUrl);

        String url = "";
        if (employee != null && employee.getId() > 0) {
    %>
    <title>Редактирование сотрудника</title>
    <%
        url = baseUrl + Pages.updateEmployeePost;
        buttonTitle = "Редактировать";
    } else {
    %>
    <title>Добавление сотрудника</title>
    <%
            url = baseUrl + Pages.addEmployeePost;
        }
    %>
</head>

<body>
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
                String employeeName = (employee != null && employee.getName() != null) ? employee.getName() : "";
            %>

            <input type="text" name="name" id="name" value="<%=employeeName%>">
        </p>

        <p>
            <label for="post">Должность</label>
            <%
                String employeePost = (employee != null && employee.getPost() != null) ? employee.getPost() : "";
            %>
            <input type="text" name="post" id="post" value="<%=employeePost%>">
        </p>

        <p>
            <%
                List<Workplace> workplaceList = (List<Workplace>) request.getAttribute(Parameters.workplaceList);
            %>

            <label>Рабочее место</label>
            <select name="<%=Parameters.workplaceId%>">
                <option value="-1"/>
                <%
                    Long workplaceId = (employee != null && employee.getWorkplace() != null) ? employee.getWorkplace().getId() : -1;
                    for (Workplace workplace : workplaceList) {
                        if (workplace.getId() == workplaceId) {
                %>
                <option selected value="<%=workplace.getId()%>"><%=workplace.getTitle()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=workplace.getId()%>"><%=workplace.getTitle()%>
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
                Role role = (Role) request.getAttribute(Parameters.role);
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" value="<%=buttonTitle%>">
            <%
                }
                String redirect = (String) request.getAttribute(Parameters.redirect);
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <input type="hidden" name="<%=Parameters.token%>" value="<%=token%>">
            <a href="<%=baseUrl+redirect%>?<%=Parameters.token%>=<%=token%>" class="button">Назад</a>
        </p>
    </div>
</form>
</body>
</html>
