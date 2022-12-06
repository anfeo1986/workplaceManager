<%@ page import="workplaceManager.controller.MainController" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <%
        String typePageStr = (String) request.getAttribute(Parameters.page);
        MainController.TypePage typePage = MainController.TypePage.valueOf(typePageStr);
        String buttonStyle = "button";
        String buttonStyleSelect = "button_select";
    %>
    <tr>

        <td><a href="/workplace?token=${token}"
               class="<%=MainController.TypePage.workplace.equals(typePage) ? buttonStyleSelect : buttonStyle%>">
            Рабочие места</a></td>
        <td><a href="/computer?token=${token}"
               class="<%=MainController.TypePage.computer.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Компьютеры</a>
        </td>
        <td><a href="/monitor?token=${token}"
               class="<%=MainController.TypePage.monitor.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Мониторы</a>
        </td>
        <td><a href="/mfd?token=${token}"
               class="<%=MainController.TypePage.mfd.equals(typePage) ? buttonStyleSelect : buttonStyle%>">МФУ</a></td>
        <td><a href="/printer?token=${token}"
               class="<%=MainController.TypePage.printer.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Принтеры</a>
        </td>
        <td><a href="/scanner?token=${token}"
               class="<%=MainController.TypePage.scanner.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Сканеры</a>
        </td>
        <td><a href="/ups?token=${token}"
               class="<%=MainController.TypePage.ups.equals(typePage) ? buttonStyleSelect : buttonStyle%>">ИБП</a></td>
        <td><a href="/employee?token=${token}"
               class="<%=MainController.TypePage.employee.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Сотрудники</a>
        </td>
        <td><a href="/accounting1c?token=${token}"
               class="<%=MainController.TypePage.accounting1c.equals(typePage) ? buttonStyleSelect : buttonStyle%>">Бухгалтерия</a>
        </td>
    </tr>
</table>
