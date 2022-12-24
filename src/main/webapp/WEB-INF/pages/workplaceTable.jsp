<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.Pages" %>

<%
    Role roleInWorklace = (Role) request.getSession().getAttribute(Parameters.role);
%>

<div align="center"><h1>Рабочие места</h1></div>

<table>
    <tr>
        <th/>
        <th>Название</th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.employee%>">Сотрудники</a>
            </p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEmployee%>?<%=Parameters.redirect%>=<%=Pages.workplace%>">
                    Добавить сотрудника</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=baseUrl + Pages.computer%>">Компьютеры</a></h1></p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>">
                    Добавить компьютер</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=baseUrl + Pages.monitor%>">Мониторы</a></h1></p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>">
                    Добавить монитор</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=baseUrl + Pages.mfd%>">МФУ</a></h1></p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>">
                    Добавить МФУ</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=baseUrl + Pages.printer%>">Принтеры</a></h1></p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>">
                    Добавить принтер</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=baseUrl + Pages.scanner%>">Сканеры</a></h1></p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>">
                    Добавить сканер</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=baseUrl + Pages.ups%>">ИБП</a></h1></p>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>">
                    Добавить ИБП</a></p>
            <%
                }
            %>
        </th>
        <%
            if (Role.ADMIN.equals(roleInWorklace)) {
        %>
        <th/>
        <%
            }
        %>
    </tr>

    <%
        List<Workplace> workplaceList = (List<Workplace>) request.getAttribute(Parameters.workplaceList);

        int count = 1;
        for (Workplace workplace : workplaceList) {
            List<Computer> computerList1 = new ArrayList<>();
            List<Monitor> monitorList = new ArrayList<>();
            List<Printer> printerList = new ArrayList<>();
            List<Scanner> scannerList = new ArrayList<>();
            List<Mfd> mfdList = new ArrayList<>();
            List<Ups> upsList = new ArrayList<>();

            if (!workplace.getEquipmentList().isEmpty()) {
                for (Equipment equipment : workplace.getEquipmentList()) {
                    if (equipment instanceof Computer) {
                        computerList1.add((Computer) equipment);
                    }
                    if (equipment instanceof Monitor) {
                        monitorList.add((Monitor) equipment);
                    }
                    if (equipment instanceof Printer) {
                        printerList.add((Printer) equipment);
                    }
                    if (equipment instanceof Scanner) {
                        scannerList.add((Scanner) equipment);
                    }
                    if (equipment instanceof Mfd) {
                        mfdList.add((Mfd) equipment);
                    }
                    if (equipment instanceof Ups) {
                        upsList.add((Ups) equipment);
                    }
                }
            }
    %>
    <tr>
        <td><%=count%>
        </td>
        <%
            count++;
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=workplace.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>">
                <%=workplace.getTitleHtml()%>
            </a></td>

        <td>
            <%
                if (!workplace.getEmployeeList().isEmpty()) {
                    for (Employee employee : workplace.getEmployeeList()) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEmployee%>?<%=Parameters.id%>=<%=employee.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>">
                    <%=employee.getName()%>
                </a></p>
            <%
                    }
                }
            %>
        </td>

        <td>
            <%
                for (Computer computer : computerList1) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>">
                    <%=computer.toStringHtmlSelectUid()%>
                </a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Monitor monitor : monitorList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=monitor.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>">
                    <%=monitor.toStringHtml()%>
                </a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Mfd mfd : mfdList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=mfd.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>">
                    <%=mfd.toStringHtml()%>
                </a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Printer printer : printerList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=printer.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>">
                    <%=printer.toStringHtml()%>
                </a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Scanner scanner : scannerList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=scanner.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>">
                    <%=scanner.toStringHtml()%>
                </a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Ups ups : upsList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=ups.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>">
                    <%=ups.toStringHtml()%>
                </a></p>
            <%
                }
            %>
        </td>

        <%
            if (Role.ADMIN.equals(roleInWorklace)) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.deleteWorkplacePost%>?<%=Parameters.id%>=<%=workplace.getId()%>&<%=Parameters.typeEquipment%>=<%=Pages.workplace%>">
                Удалить</a></td>
        <%
            }
        %>
    </tr>
    <%
        }
    %>

</table>