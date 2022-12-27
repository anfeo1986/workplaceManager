<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.ReplaceString" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Role roleInAccounting1C = (Role) request.getSession().getAttribute(Parameters.role);
    Long idAccounting1C = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
%>

<div align="center"><h1>Бухгалтерия</h1></div>
<%
    if (idAccounting1C == null) {
%>
<%@include file='/WEB-INF/pages/filter/accounting1CFilter.jsp' %>
<%
    }
%>
<table>
    <tr>
        <th>ID</th>
        <th>Инвентарный номер</th>
        <th>Название</th>
        <th>Материально-отвественное лицо</th>
        <th>Оборудование</th>
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
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>"
               target="_blank">
                <%=accounting1C.getInventoryNumber()%>
            </a>
        </td>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=accounting1C.getId()%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>"
               target="_blank">
                <%=accounting1C.getTitleHtml()%>
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
                    String pageEquipment = "";
                    if (equipment instanceof Computer) {
                        typeEquipment = TypeEquipment.COMPUTER;
                        pageEquipment = Pages.computer;
                    }
                    if (equipment instanceof Monitor) {
                        typeEquipment = TypeEquipment.MONITOR;
                        pageEquipment = Pages.monitor;
                    }
                    if (equipment instanceof Printer) {
                        typeEquipment = TypeEquipment.PRINTER;
                        pageEquipment = Pages.printer;
                    }
                    if (equipment instanceof Scanner) {
                        typeEquipment = TypeEquipment.SCANNER;
                        pageEquipment = Pages.scanner;
                    }
                    if (equipment instanceof Mfd) {
                        typeEquipment = TypeEquipment.MFD;
                        pageEquipment = Pages.mfd;
                    }
                    if (equipment instanceof Ups) {
                        typeEquipment = TypeEquipment.UPS;
                        pageEquipment = Pages.ups;
                    }
            %>
            <p>

                <a href="<%=baseUrl + pageEquipment%>?<%=Parameters.id%>=<%=equipment.getId()%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
                   target="_blank">
                    <%=equipment.toStringHtml()%>
                </a><br>
                Рабочее место:
                <b><%=equipment.getWorkplace() != null ? equipment.getWorkplace().toStringHtml() : ""%>
                </b>
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
    </tr>
    <%
        }
    %>
</table>

<%
    if (idAccounting1C != null && idAccounting1C > 0) {
%>
<div align="center">
    <p>
        <a onclick="close_window(); return false;" class="button">Назад</a>
    </p>
</div>
<%
    }
%>
