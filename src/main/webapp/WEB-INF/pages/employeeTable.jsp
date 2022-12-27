<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="workplaceManager.db.domain.Accounting1C" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    List<Employee> employeeList = request.getAttribute(Parameters.employeeList) != null ?
            (List<Employee>) request.getAttribute(Parameters.employeeList) : new ArrayList<>();
    Role roleInEmployee = request.getSession().getAttribute(Parameters.role) != null ?
            (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;
    Long idEmployee = request.getParameter(Parameters.id) != null ?
            Long.parseLong(request.getParameter(Parameters.id)) : null;
%>

<div align="center"><h1>Сотрудники</h1></div>
<%
    if(idEmployee == null) {
%>
<%
    }
%>

<table>
    <tr>
        <th>Номер</th>
        <th>ФИО</th>
        <th>Должность</th>
        <th><a href="<%=baseUrl+Pages.workplace%>">Рабочее место</a></th>
        <th><a href="<%=baseUrl+Pages.accounting1c%>">Бухгалтерия</a></th>
    </tr>
    <%
        int count = 1;
        for (Employee employee : employeeList) {
    %>
    <tr>
        <td><%=count++%>
        </td>

        <td>
            <a href="<%=baseUrl+Pages.addUpdateEmployee%>?<%=Parameters.id%>=<%=employee.getId()%>&<%=Parameters.redirect%>=<%=Pages.employee%>"
               target="_blank">
                <%=employee.getName()%>
            </a></td>
        <td><%=employee.getPost()%>
        </td>
        <%
            if (employee.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=baseUrl+Pages.workplace%>?<%=Parameters.id%>=<%=employee.getWorkplace().getId()%>&<%=Parameters.redirect%>=<%=Pages.employee%>"
               target="_blank">
                <%=employee.getWorkplace().getTitleHtml()%>
            </a></td>
        <%
        } else {
        %>
        <td/>
        <%
            }
            if (employee.getAccounting1СList() != null) {
        %>
        <td>
            <%
                for (Accounting1C accounting1C : employee.getAccounting1СList()) {
            %>
            <p>
                <a href="<%=baseUrl+Pages.accounting1c%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.redirect%>=<%=Pages.employee%>"
                   target="_blank">
                    <%=accounting1C.toStringHtml()%>
                </a>
            </p>
            <%
                }
            %>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>

    </tr>
    <%
        }
    %>
</table>

<%
    if (idEmployee != null && idEmployee > 0) {
%>
<div align="center">
    <p>
        <a onclick="close_window(); return false;" class="button">Назад</a>
    </p>
</div>
<%
    }
%>