<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.ReplaceString" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="center"><h1>Бухгалтерия</h1></div>

<table>
    <tr>
        <th>ID</th>
        <th>Инвентарный номер</th>
        <th>Название</th>
        <th>Материально-отвественное лицо</th>
        <th>Оборудование</th>
        <%
            if (Role.ADMIN.equals(role)) {
        %>
        <th/>
        <%
            }
        %>
    </tr>

    <%
        List<Accounting1C> accounting1CList = (List<Accounting1C>) request.getAttribute(Parameters.accounting1CList);
        int count = 1;
        for (Accounting1C accounting1C : accounting1CList) {
    %>
    <tr>
        <td><%=count%>
        </td>
        <%
            count++;
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>">
                <%=accounting1C.getInventoryNumber()%>
            </a>
        </td>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>">
                <%=ReplaceString.replace(accounting1C.getTitle())%>
            </a>
        </td>
        <%
            if (accounting1C.getEmployee() != null) {
        %>
        <td><%=accounting1C.getEmployee().getName()%>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <%
            if (!accounting1C.getEquipmentList().isEmpty()) {
        %>
        <td>
            <%
                for (Equipment equipment : accounting1C.getEquipmentList()) {
                    String typeEquipment = "";
                    if (equipment instanceof Computer) {
                        typeEquipment = TypeEquipment.COMPUTER;
                    }
                    if (equipment instanceof Monitor) {
                        typeEquipment = TypeEquipment.MONITOR;
                    }
                    if (equipment instanceof Printer) {
                        typeEquipment = TypeEquipment.PRINTER;
                    }
                    if (equipment instanceof Scanner) {
                        typeEquipment = TypeEquipment.SCANNER;
                    }
                    if (equipment instanceof Mfd) {
                        typeEquipment = TypeEquipment.MFD;
                    }
                    if (equipment instanceof Ups) {
                        typeEquipment = TypeEquipment.UPS;
                    }
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                    <%=equipment%>
                </a><br>
                <b>Рабочее
                    место: </b> <%=equipment.getWorkplace() != null ? ReplaceString.replace(equipment.getWorkplace().toString()) : ""%>
                <br>

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
            <a href="<%=baseUrl + Pages.deleteAccounting1CPost%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>&<%=Parameters.token%>=<%=token%>">
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
