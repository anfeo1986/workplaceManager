<%@ page import="workplaceManager.db.domain.Employee" %>
<%@ page import="workplaceManager.db.domain.Accounting1C" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.db.domain.VirtualMachine" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    List<VirtualMachine> virtualMachineList = request.getAttribute(Parameters.virtualMachineList) != null ?
            (List<VirtualMachine>) request.getAttribute(Parameters.virtualMachineList) : new ArrayList<>();
%>

<div align="center"><h1>Виртуальные машины</h1></div>

<table>
    <tr>
        <th>Номер</th>
        <th>IP</th>
        <th>Описание</th>
        <th>Тип ОС</th>
        <th>ОС</th>
        <th>Компьютер</th>
    </tr>
    <%
        int count = 1;
        for (VirtualMachine virtualMachine : virtualMachineList) {
    %>
    <tr>
        <td><%=count++%>
        </td>

        <td>
            <%=virtualMachine.getIp() != null ? virtualMachine.getIp() : ""%>
        </td>
        <td>
            <%=virtualMachine.getDescriptionHtml()%>
        </td>
        <td>
            <%=virtualMachine.getTypeOS() != null ? virtualMachine.getTypeOS() : ""%>
        </td>
        <td>
            <%=virtualMachine.getOs()%>
        </td>
        <td>
            <%
                Long idComputer = virtualMachine.getComputer() != null ? virtualMachine.getComputer().getId() : -1L;
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=idComputer%>&<%=Parameters.redirect%>=<%=Pages.virtualMachine%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>"
               target="_blank">
                <%=virtualMachine.getComputer() != null ?
                        String.format("%s (%s)", virtualMachine.getComputer().getIp(), virtualMachine.getComputer().getNetName()) : ""%>
            </a>
        </td>
    </tr>
    <%
        }
    %>
</table>
