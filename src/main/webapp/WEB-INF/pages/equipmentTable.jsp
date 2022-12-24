<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="center"><h1>${title}</h1></div>

<%
    String typeEquipment = (String) request.getAttribute(Parameters.typeEquipment);
    String redirect = (String) request.getAttribute(Parameters.page);
    String title = (String) request.getAttribute(Parameters.title);
    List<Equipment> equipmentList = (List<Equipment>) request.getAttribute(Parameters.equipmentList);
    Role roleInEquipment = (Role) request.getSession().getAttribute(Parameters.role);

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


<table class="table">
    <tr>
        <th>id</th>
        <th><%=title%></th>
        <th>Рабочее место</th>
        <th>Комментарий</th>
        <th>Бухгалтерия</th>
        <%
            if(Role.ADMIN.equals(roleInEquipment)) {
        %>
        <th/>
        <%
            }
        %>
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
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                <%=equipment.toStringHtml()%>
            </a>
        </td>
        <%
            if (equipment.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=equipment.getWorkplace().getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
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
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=equipment.getAccounting1C().getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
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
        <%
            if (Role.ADMIN.equals(roleInEquipment)) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.deleteEquipmentPost%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
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