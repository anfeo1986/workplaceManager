<%@ page import="workplaceManager.controller.MainController" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page import="org.springframework.data.domain.Page" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <%
        String typePageStr = (String) request.getAttribute(Parameters.page);
        MainController.TypePage typePage = MainController.TypePage.valueOf(typePageStr);
        String buttonStyle = "button";
        String buttonStyleSelect = "button_select";
        String token=(String) request.getAttribute(Parameters.token);
    %>
    <tr>

        <td><a href="<%=Pages.workplace%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.workplace.equals(typePage) ? buttonStyleSelect : buttonStyle%>">
            Рабочие места</a></td>
        <td><a href="<%=Pages.computer%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.computer.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Компьютеры</a>
        </td>
        <td><a href="<%=Pages.monitor%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.monitor.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Мониторы</a>
        </td>
        <td><a href="<%=Pages.mfd%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.mfd.equals(typePage) ? buttonStyleSelect : buttonStyle%>">МФУ</a></td>
        <td><a href="<%=Pages.printer%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.printer.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Принтеры</a>
        </td>
        <td><a href="<%=Pages.scanner%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.scanner.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Сканеры</a>
        </td>
        <td><a href="<%=Pages.ups%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.ups.equals(typePage) ? buttonStyleSelect : buttonStyle%>">ИБП</a></td>
        <td><a href="<%=Pages.employee%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.employee.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Сотрудники</a>
        </td>
        <td><a href="<%=Pages.accounting1c%>?<%=Parameters.token%>=<%=token%>"
               class="<%=MainController.TypePage.accounting1c.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Бухгалтерия</a>
        </td>
    </tr>
</table>
