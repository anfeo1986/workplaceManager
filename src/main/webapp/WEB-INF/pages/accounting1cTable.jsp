<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Бухгалтерия</h1>
<%
    String token = (String) request.getAttribute("token");
    Role role = (Role) request.getAttribute("role");

    if (Role.ADMIN.equals(role)) {
%>
<a href="<%=Pages.addUpdateAccounting1C%>?redirect=<%=Pages.accounting1c%>&token=<%=token%>">
    Добавить оборудование в бухгалтерию
</a>
<%
    }
%>

<table>
    <tr>
        <th><h1>ID</h1></th>
        <th><h1>Инвентарный номер</h1></th>
        <th><h1>Название</h1></th>
        <th><h1>Материально-отвественное лицо</h1></th>
        <th><h1>Оборудование</h1></th>
    </tr>

    <%
        List<Accounting1C> accounting1CList = (List<Accounting1C>) request.getAttribute("accounting1CList");
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
            <a href="<%=Pages.addUpdateAccounting1C%>?id=<%=accounting1C.getId()%>&token=<%=token%>&redirect=<%=Pages.accounting1c%>">
                <%=accounting1C.getInventoryNumber()%>
            </a>
        </td>
        <td>
            <a href="<%=Pages.addUpdateAccounting1C%>?id=<%=accounting1C.getId()%>&token=<%=token%>&redirect=<%=Pages.accounting1c%>">
                <%=accounting1C.getTitle()%>
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
                <a href="<%=Pages.addUpdateEquipment%>?id=<%=equipment.getId()%>&token=<%=token%>&redirect=<%=Pages.accounting1c%>&typeEquipment=<%=typeEquipment%>">
                    <%=equipment%>
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
            <a href="<%=Pages.deleteAccounting1CPost%>?id=<%=accounting1C.getId()%>&redirect=<%=Pages.accounting1c%>&token=<%=token%>">
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
