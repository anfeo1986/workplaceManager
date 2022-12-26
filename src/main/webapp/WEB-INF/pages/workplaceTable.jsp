<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.Pages" %>

<div align="center"><h1>Рабочие места</h1></div>

<%
    Role roleInWorklace = (Role) request.getSession().getAttribute(Parameters.role);
    Long idWorkplace = request.getParameter(Parameters.id) != null ? Long.parseLong(request.getParameter(Parameters.id)) : null;
%>


<table>
    <tr>
        <th/>
        <th>Название</th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.employee%>">Сотрудники</a>
            </p>
        </th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.computer%>">Компьютеры</a></p>

        </th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.monitor%>">Мониторы</a></p>
        </th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.mfd%>">МФУ</a></p>
        </th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.printer%>">Принтеры</a></p>
        </th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.scanner%>">Сканеры</a></p>
        </th>
        <th>
            <p>
                <a href="<%=baseUrl + Pages.ups%>">ИБП</a></p>
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
            <a href="<%=baseUrl + Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=workplace.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>"
               target="_blank">
                <%=workplace.getTitleHtml()%>
            </a></td>

        <td>
            <%
                if (!workplace.getEmployeeList().isEmpty()) {
                    for (Employee employee : workplace.getEmployeeList()) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.employee%>?<%=Parameters.id%>=<%=employee.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>">
                    <%=employee.getName()%>
                </a></p>
            <%
                    }
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEmployee%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить сотрудника</a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Computer computer : computerList1) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.computer%>?<%=Parameters.id%>=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>">
                    <%=computer.toStringHtmlSelectUid()%>
                </a></p>
            <%
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить компьютер</a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Monitor monitor : monitorList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.monitor%>?<%=Parameters.id%>=<%=monitor.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>">
                    <%=monitor.toStringHtml()%>
                </a></p>
            <%
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить монитор</a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Mfd mfd : mfdList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.mfd%>?<%=Parameters.id%>=<%=mfd.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>">
                    <%=mfd.toStringHtml()%>
                </a></p>
            <%
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить МФУ</a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Printer printer : printerList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.printer%>?<%=Parameters.id%>=<%=printer.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>">
                    <%=printer.toStringHtml()%>
                </a></p>
            <%
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить принтер</a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Scanner scanner : scannerList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.scanner%>?<%=Parameters.id%>=<%=scanner.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>">
                    <%=scanner.toStringHtml()%>
                </a></p>
            <%
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить сканер</a></p>
            <%
                }
            %>
        </td>

        <td>
            <%
                for (Ups ups : upsList) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.ups%>?<%=Parameters.id%>=<%=ups.getId()%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>">
                    <%=ups.toStringHtml()%>
                </a></p>
            <%
                }
            %>
            <%
                if (Role.ADMIN.equals(roleInWorklace)) {
            %>
            <p>
                <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>&<%=Parameters.workplaceId%>=<%=workplace.getId()%>"
                   style="color: cornflowerblue; font-family: Arial, Helvetica, sans-serif;font-family: Arial, Helvetica, sans-serif; font-size: 10px;">
                    Добавить ИБП</a></p>
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