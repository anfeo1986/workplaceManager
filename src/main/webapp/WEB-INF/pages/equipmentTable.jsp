<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${title}</h1>

<%
    Role role = (Role) request.getAttribute(Parameters.role);
    String typeEquipment = (String) request.getAttribute(Parameters.typeEquipment);
    String redirect = (String) request.getAttribute(Parameters.page);
    String title = (String) request.getAttribute(Parameters.title);
    List<Equipment> equipmentList = (List<Equipment>) request.getAttribute(Parameters.equipmentList);

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

    if (Role.ADMIN.equals(role)) {
%>
<a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>
&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
    Добавить <%=typeEquipmentStr%>
</a>
<%
    }
%>

<table class="table">
    <tr>
        <th><h1>id</h1></th>
        <th><h1><%=title%>
        </h1></th>
        <th><h1>Рабочее место</h1></th>
        <th><h1>Бухгалтерия</h1></th>
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
            <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.token%>=<%=token%>
            &<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                <%=equipment%>
            </a>
        </td>
        <%
            if (equipment.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=equipment.getWorkplace().getId()%>
        &<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                <%=equipment.getWorkplace().getTitle()%>
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
            if (equipment.getAccounting1C() != null) {
        %>
        <td>
            <a href="<%=Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=equipment.getAccounting1C().getId()%>
        &<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                <%=equipment.getAccounting1C()%>
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
            if (Role.ADMIN.equals(role)) {
        %>
        <td>
            <a href="<%=Pages.deleteEquipmentPost%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.token%>=<%=token%>
            &<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
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