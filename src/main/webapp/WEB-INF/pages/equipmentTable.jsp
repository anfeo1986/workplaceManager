<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%
    String typeEquipment = (String) request.getAttribute(Parameters.typeEquipment);
    String redirect = (String) request.getAttribute(Parameters.page);
    String title = (String) request.getAttribute(Parameters.title);
    List<Equipment> equipmentList = (List<Equipment>) request.getAttribute(Parameters.equipmentList);
    Role roleInEquipment = (Role) request.getSession().getAttribute(Parameters.role);
    Long idEquipment = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;

    String typeEquipmentStr = "";
    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
        typeEquipmentStr = "компьютер";
    } else if (TypeEquipment.MONITOR.equals(typeEquipment)) {
        typeEquipmentStr = "монитор";
    } else if (TypeEquipment.MFD.equals(typeEquipment)) {
        typeEquipmentStr = "МФУ";
    } else if (TypeEquipment.PRINTER.equals(typeEquipment)) {
        typeEquipmentStr = "принтер";
    } else if (TypeEquipment.SCANNER.equals(typeEquipment)) {
        typeEquipmentStr = "сканер";
    } else if (TypeEquipment.UPS.equals(typeEquipment)) {
        typeEquipmentStr = "ИБП";
    }
%>

<div align="center"><h1>${title}</h1></div>

<%
    if(idEquipment == null) {
%>
<%
    }
%>

<table class="table">
    <tr>
        <th>Номер</th>
        <th><%=title%></th>
        <th>Рабочее место</th>
        <th>Комментарий</th>
        <th>Бухгалтерия</th>
    </tr>

    <%
        int count = 1;
        for (Equipment equipment : equipmentList) {
    %>
    <tr>
        <td><%=count%>
        </td>
        <%
            count++;
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=equipment.toStringHtml()%>
            </a>
        </td>
        <%
            if (equipment.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.workplace%>?<%=Parameters.id%>=<%=equipment.getWorkplace().getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=equipment.getWorkplace().getTitleHtml()%>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <td>
            <%=equipment.getCommentHtml()%>
        </td>
        <%
            if (equipment.getAccounting1C() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.accounting1c%>?<%=Parameters.id%>=<%=equipment.getAccounting1C().getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=equipment.getAccounting1C().toStringHtml()%>
            </a>
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
    if (idEquipment != null && idEquipment > 0) {
%>
<div align="center">
    <p>
        <a onclick="close_window(); return false;" class="button">Назад</a>
    </p>
</div>
<%
    }
%>