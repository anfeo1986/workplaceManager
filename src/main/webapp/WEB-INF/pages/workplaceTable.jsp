<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.Pages" %>


<h1>Рабочие места</h1>
<%
    Role role = (Role) request.getAttribute(Parameters.role);

    if (Role.ADMIN.equals(role)) {
%>
<a href="<%=Pages.addUpdateWorkplace%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.workplace%>">
    Добавить рабочее место
</a>
<%
    }
%>

<table>
    <tr>
        <th/>
        <th><h1>Название</h1></th>
        <th>
            <p>
            <h1><a href="<%=Pages.employee%>?<%=Parameters.token%>=<%=token%>">Сотрудники</a></h1>
            </p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p><a href="<%=Pages.addUpdateEmployee%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.token%>=<%=token%>">Добавить
                сотрудника</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=Pages.computer%>?<%=Parameters.token%>=<%=token%>">Компьютеры</a></h1></p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>
            &<%=Parameters.token%>=<%=token%>">Добавить компьютер</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=Pages.monitor%>?<%=Parameters.token%>=<%=token%>">Мониторы</a></h1></p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>
            &<%=Parameters.token%>=<%=token%>">Добавить монитор</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=Pages.mfd%>?<%=Parameters.token%>=<%=token%>">МФУ</a></h1></p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>
                &<%=Parameters.token%>=<%=token%>">
                    Добавить МФУ</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=Pages.printer%>?<%=Parameters.token%>="<%=token%>">Принтеры</a></h1></p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>
                &<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>&<%=Parameters.token%>=<%=token%>">Добавить
                    принтер</a></p>
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=Pages.scanner%>?<%=Parameters.token%>=<%=token%>">Сканеры</a></h1></p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>
                &<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>&<%=Parameters.token%>=<%=token%>">
                    Добавить сканер</a></p>");
            <%
                }
            %>
        </th>
        <th>
            <p>
            <h1><a href="<%=Pages.ups%>?<%=Parameters.token%>=<%=token%>">ИБП</a></h1></p>
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.workplace%>
                &<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>&<%=Parameters.token%>=<%=token%>">Добавить
                    ИБП</a></p>
            <%
                }
            %>
        </th>
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
            <a href="<%=Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=workplace.getId()%>
            &<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.token%>=<%=token%>">
                <%=workplace.getTitle()%>
            </a></td>

        <td>
            <%
                if (!workplace.getEmployeeList().isEmpty()) {
                    for (Employee employee : workplace.getEmployeeList()) {
            %>
            <p>
                <a href="<%=Pages.addUpdateEmployee%>?<%=Parameters.id%>=<%=employee.getId()%>
                &<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.workplace%>">
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
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=computer.getId()%>&<%=Parameters.token%>=<%=token%>
                &<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>">
                    <%=computer%>
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
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=monitor.getId()%>
                &<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>">
                    <%=monitor%>
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
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=mfd.getId()%>&<%=Parameters.token%>=<%=token%>
                &<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>">
                    <%=mfd%>
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
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=printer.getId()%>&<%=Parameters.token%>=<%=token%>
                &<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>">
                    <%=printer%>
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
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=scanner.getId()%>&<%=Parameters.token%>=<%=token%>
                &<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>">
                    <%=scanner%>
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
                <a href="<%=Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=ups.getId()%>&<%=Parameters.token%>=<%=token%>
                &<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>">
                    <%=ups%>
                </a></p>"
            <%
                }
            %>
        </td>

        <%
            if (Role.ADMIN.equals(role)) {
        %>
        <td>
            <a href="<%=Pages.deleteWorkplacePost%>?<%=Parameters.id%>=<%=workplace.getId()%>
            &<%=Parameters.typeEquipment%>=<%=Pages.workplace%>&<%=Parameters.token%>=<%=token%>">
                Удалить</a></td>
        <%
            }
        %>
    </tr>
    <%
        }
    %>

</table>