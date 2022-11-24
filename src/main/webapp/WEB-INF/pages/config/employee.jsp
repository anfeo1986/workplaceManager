<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.db.domain.Workplace" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <%
        Employee employee = (Employee) request.getAttribute("employee");
        String buttonTitle = "Добавить";
        String token = (String) request.getAttribute("token");

        String url = "";
        if (employee != null && employee.getId() > 0) {
            out.println("<title>Редактирование сотрудника</title>");
            url = "/" + Pages.updateEmployeePost;
            buttonTitle = "Редактировать";
        } else {
            out.println("<title>Добавление сотрудника</title>");
            url = "/" + Pages.addEmployeePost;
        }
    %>

</head>

<body>
<%
    String error = (String) request.getAttribute("error");
    if (error != null && error != "") {
        out.println("<h3>" + error + "</h3>");
    }
    String message = (String) request.getAttribute("message");
    if (message != null && message != "") {
        out.println("<h3>" + message + "</h3>");
    }

    out.println("<form action=\"" + url + "\" method=\"post\">");
    if (employee != null && employee.getId() > 0) {
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + employee.getId() + "\">");
    }

    out.println("<div class=\"wrapper_500\">");
    out.println("<p>");
    out.println("<label for=\"name\">ФИО</label>");
    String employeeName = (employee != null && employee.getName() != null) ? employee.getName() : "";
    out.println("<input type=\"text\" name=\"name\" id=\"name\" value=\"" + employeeName + "\">");
    out.println("</p>");

    out.println("<p>");
    out.println("<label for=\"post\">Должность</label>");
    String employeePost = (employee != null && employee.getPost() != null) ? employee.getPost() : "";
    out.println("<input type=\"text\" name=\"post\" id=\"post\" value=\"" + employeePost + "\">");
    out.println("</p>");

    out.println("<p>");
    List<Workplace> workplaceList = (List<Workplace>) request.getAttribute("workplaceList");
    out.println("<label>Рабочее место</label>");
    out.println("<select name=\"workplace_id\">");
    out.println("<option value=\"-1\"/>");
    Long workplaceId = (employee != null && employee.getWorkplace() != null) ? employee.getWorkplace().getId() : -1;
    for (Workplace workplace : workplaceList) {
        if (workplace.getId() == workplaceId) {
            out.println("<option selected value=\"" + workplace.getId() + "\">" + workplace.getTitle() + "</option>");
        } else {
            out.println("<option value=\"" + workplace.getId() + "\">" + workplace.getTitle() + "</option>");
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
