<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="workplaceManager.db.domain.Accounting1C" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="center"><h1>Сотрудники</h1></div>
<%
    List<Employee> employeeList = (List<Employee>) request.getAttribute(Parameters.employeeList);
    Role roleInEmployee = (Role) request.getSession().getAttribute(Parameters.role);

%>

<table>
    <tr>
        <th>id</th>
        <th>ФИО</th>
        <th>Должность</th>
        <th><a href="<%=baseUrl+Pages.workplace%>">Рабочее место</a></th>
        <th><a href="<%=baseUrl+Pages.accounting1c%>">Бухгалтерия</a></th>
        <%
            if(Role.ADMIN.equals(roleInEmployee)) {
        %>
        <th/>
        <%
            }
        %>
    </tr>
    <%
        for (Employee employee : employeeList) {
    %>
    <tr>
        <td><%=employee.getId()%>
        </td>
        <td>
            <a href="<%=baseUrl+Pages.addUpdateEmployee%>?<%=Parameters.id%>=<%=employee.getId()%>&<%=Parameters.redirect%>=<%=Pages.employee%>">
                <%=employee.getName()%>
            </a></td>
        <td><%=employee.getPost()%>
        </td>
        <%
            if (employee.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=baseUrl+Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=employee.getWorkplace().getId()%>&<%=Parameters.redirect%>=<%=Pages.employee%>">
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
                <a href="<%=baseUrl+Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.redirect%>=<%=Pages.employee%>">
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
        <%
            if (Role.ADMIN.equals(roleInEmployee)) {
        %>
        <td>
            <a href="<%=baseUrl+Pages.deleteEmployeePost%>?<%=Parameters.id%>=<%=employee.getId()%>">
                Удалить
            </a>
        </td>
        <%
            }
        %>
    </tr>
    <%
        }
    %>
</table>