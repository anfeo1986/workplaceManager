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
            out.println("<title>Редактирование</title>");
            url = "/" + Pages.updateAccounting1CPost;
            buttonTitle = "Редактировать";
        } else {
            out.println("<title>Добавление</title>");
            url = "/" + Pages.addAccounting1CPost;
        }
    %>
</head>

<body>
<%
    if (error != null && error != "") {
        out.println("<h3>" + error + "</h3>");
    }
    if (message != null && message != "") {
        out.println("<h3>" + message + "</h3>");
    }

    out.println("<form action=\"" + url + "\" method=\"post\">");
    if (accounting1C != null && accounting1C.getId() > 0) {
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + accounting1C.getId() + "\">");
    }

    out.println("<div class=\"wrapper_500\">");

    out.println("<p>");
    out.println("<label for=\"inventoryNumber\">Инвентарный номер</label>");
    String inventoryNumber = (accounting1C != null && accounting1C.getInventoryNumber() != null)
            ? accounting1C.getInventoryNumber() : "";
    out.println("<input type=\"text\" name=\"inventoryNumber\" id=\"inventoryNumber\" value=\"" + inventoryNumber + "\">");
    out.println("</p>");

    out.println("<p>");
    out.println("<label for=\"title\">Название</label>");
    String title = (accounting1C != null && accounting1C.getTitle() != null) ? accounting1C.getTitle() : "";
    out.println("<input type=\"text\" name=\"title\" id=\"title\" value=\"" + title + "\">");
    out.println("</p>");

    out.println("<p>");
    Long employeeId = (accounting1C != null && accounting1C.getEmployee() != null) ? accounting1C.getEmployee().getId() : -1;
    out.println("<label for=\"employee_id\">Материально-ответственное лицо</label>");
    out.println("<select name=\"employee_id\" id=\"employee_id\">");
    out.println("<option value=\"-1\"/>");
    List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
    for (Employee employee : employeeList) {
        if (employee.getId() == employeeId) {
            out.println("<option selected value=\"" + employee.getId() + "\">" + employee.getName() + "</option>");
        } else {
            out.println("<option value=\"" + employee.getId() + "\">" + employee.getName() + "</option>");
        }
    }
    out.println("</select>");
    out.println("</p>");
    out.println("</div>");

    out.println("<div align=\"center\">");
    out.println("<p>");
    Role role = (Role) request.getAttribute("role");
    if (Role.ADMIN.equals(role)) {
        out.println("<input type=\"submit\" value=\"" + buttonTitle + "\">");
    }
    String redirect = (String) request.getAttribute("redirect");
    out.println("<input type=\"hidden\" name=\"redirect\" value=\"" + redirect + "\">");
    out.println("<input type=\"hidden\" name=\"token\" value=\"" + token + "\">");
    out.println("<a href=\"/" + redirect + "?token=" + token + "\" class=\"button\">Назад</a>");
    out.println("</p>");
    out.println("</div>");

    out.println("</form>");
%>

</body>
</html>