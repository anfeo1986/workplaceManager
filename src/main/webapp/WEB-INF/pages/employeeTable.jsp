<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Сотрудники</h1>
<%
    Role role = (Role) request.getAttribute(Parameters.role);
    List<Employee> employeeList = (List<Employee>) request.getAttribute(Parameters.employeeList);
    String baseUrl = (String) request.getAttribute(Parameters.baseUrl);

    if (Role.ADMIN.equals(role)) {
%>
<a href="<%=Pages.addUpdateEmployee%>?<%=Parameters.redirect%>=<%=Pages.employee%>&<%=Parameters.token%>=<%=token%>">
    Добавить сотрудника
</a>
<%
    }
%>

<table>
    <tr>
        <th><h1>id</h1></th>
        <th><h1>ФИО</h1></th>
        <th><h1>Должность</h1></th>
        <th><h1><a href="<%=baseUrl+Pages.workplace%>?<%=Parameters.token%>=<%=token%>">Рабочее место</a></h1></th>
        <th><h1><a href="<%=baseUrl+Pages.accounting1c%>?<%=Parameters.token%>=<%=token%>">Бухгалтерия</a></h1></th>
    </tr>
    <%
        for (Employee employee : employeeList) {
    %>
    <tr>
        <td><%=employee.getId()%>
        </td>
        <td>
            <a href="<%=baseUrl+Pages.addUpdateEmployee%>?<%=Parameters.id%>=<%=employee.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.employee%>"><%=employee.getName()%>
            </a></td>
        <td><%=employee.getPost()%>
        </td>
        <%
            if (employee.getWorkplace() != null) {
        %>
        <td><a href="<%=baseUrl+Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=employee.getWorkplace().getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.employee%>"><%=employee.getWorkplace().getTitle()%>
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
                <a href="<%=baseUrl+Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.employee%>">
                    <%=accounting1C%>
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
            if (Role.ADMIN.equals(role)) {
        %>
        <td>
            <a href="<%=baseUrl+Pages.deleteEmployeePost%>?<%=Parameters.id%>=<%=employee.getId()%>&<%=Parameters.token%>=<%=token%>">
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