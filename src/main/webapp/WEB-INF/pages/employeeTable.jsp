<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Сотрудники</h1>
<%
    String token = (String) request.getAttribute("token");
    Role role = (Role) request.getAttribute("role");
    List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");

    if (Role.ADMIN.equals(role)) {
        out.println("<a href=\"/" + Pages.addUpdateEmployee +
                "?redirect=" + Pages.employee +
                "&token=" + token + "\">Добавить сотрудника</a>");
    }
%>

<table>
    <tr>
        <th><h1>id</h1></th>
        <th><h1>ФИО</h1></th>
        <th><h1>Должность</h1></th>
        <th><h1><a href="/<%=Pages.workplace%>?token=<%=token%>">Рабочее место</a></h1></th>
        <th><h1><a href="/<%=Pages.accounting1c%>?token=<%=token%>">Бухгалтерия</a></h1></th>
        <%
            //    out.println("<th><h1><a href=\"/" + Pages.workplace +
            //            "?token=" + token + "\">Рабочее место</a></h1></th>");
        %>
    </tr>
    <%
        for (Employee employee : employeeList) {
    %>
    <tr>
        <td><%=employee.getId()%>
        </td>
        <td>
            <a href="/<%=Pages.addUpdateEmployee%>?id=<%=employee.getId()%>&token=<%=token%>&redirect=<%=Pages.employee%>"><%=employee.getName()%>
            </a></td>
        <td><%=employee.getPost()%>
        </td>
        <%
            if (employee.getWorkplace() != null) {
        %>
        <td><a href="/<%=Pages.addUpdateWorkplace%>?id="
               <%=employee.getWorkplace().getId()%>&token=<%=token%>&redirect="<%=Pages.employee%>"><%=employee.getWorkplace().getTitle()%>
        </a></td>
        <%
        } else {
        %>
        <td/>
        <%
            }
            if (employee.getAccounting1СList() != null) {
                out.println("<td>");
                for (Accounting1C accounting1C : employee.getAccounting1СList()) {
                    out.println("<p><a href=\"/" + Pages.addUpdateAccounting1C +
                            "?id=" + accounting1C.getId() +
                            "&token=" + token +
                            "&redirect=" + Pages.employee + "\">" + accounting1C + "</a></p>");
                }
                out.println("</td>");
            } else {
                out.println("<td/>");
            }

            if (Role.ADMIN.equals(role)) {
                out.println("<td><a href=\"/" + Pages.deleteEmployeePost + "?id=" + employee.getId() + "&token=" + token + "\">Удалить</a></td>");
            }
        %>
    </tr>
    <%
        }
    %>
</table>