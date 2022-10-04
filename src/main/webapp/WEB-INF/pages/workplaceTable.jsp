<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="java.util.ArrayList" %>


<h1>Рабочие места</h1>
<a href="/config/workplace/addUpdateWorkplace">Добавить рабочее место</a>

<table>
    <tr>
        <th/>
        <th><h1>Название</h1></th>
        <th><h1><a href="/employee">Сотрудники</a></h1></th>
        <th><h1>Компьютеры</h1></th>
        <th>
            <p><h1>Мониторы</h1></p>
            <p><a href="/config/equipment/addUpdateEquipment?redirect=workplace&typeEquipment=monitor">Добавить монитор</a></p>
        </th>
        <th>
            <p><h1>МФУ</h1></p>
            <p><a href="/config/equipment/addUpdateEquipment?redirect=workplace&typeEquipment=mfd">Добавить МФУ</a></p>
        </th>
        <th>
            <p><h1>Принтеры</h1></p>
            <p><a href="/config/equipment/addUpdateEquipment?redirect=workplace&typeEquipment=printer">Добавить принтер</a></p>
        </th>
        <th>
            <p><h1>Сканеры</h1></p>
            <p><a href="/config/equipment/addUpdateEquipment?redirect=workplace&typeEquipment=scanner">Добавить сканер</a></p>
        </th>
        <th>
            <p><h1>ИБП</h1></p>
            <p><a href="/config/equipment/addUpdateEquipment?redirect=workplace&typeEquipment=ups">Добавить ИБП</a></p>
        </th>
    </tr>

    <%
        List<Workplace> workplaceList = (List<Workplace>) request.getAttribute("workplaceList");
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
                    if(equipment instanceof Computer) {
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

            out.println("<tr>");

            out.println("<td>" + count + "</td>");
            count++;

            out.println("<td><a href=\"/config/workplace/addUpdateWorkplace?id=" + workplace.getId() + "\">" + workplace.getTitle() + "</a></td>");

            out.println("<td>");
            if (!workplace.getEmployeeList().isEmpty()) {
                for (Employee employee : workplace.getEmployeeList()) {
                    out.println("<p><a href=\"/config/employee/addUpdateEmployee?id=" + employee.getId() + "&redirect=workplace\">" + employee.getName() + "</a></p>");
                }
            }
            out.println("</td>");

            //компьютеры
            out.println("<td>");
            for (Computer computer : computerList1) {
                out.println("<p><a href=\"/config/equipment/addUpdateEquipment?id=" + computer.getId() +
                        "&redirect=workplace&typeEquipment=computer\">" + computer + "</a></p>");
            }
            out.println("</td>");

            //мониторы
            out.println("<td>");
            for (Monitor monitor : monitorList) {
                out.println("<p><a href=\"/config/equipment/addUpdateEquipment?id=" + monitor.getId() +
                        "&redirect=workplace&typeEquipment=monitor\">" + monitor + "</a></p>");
            }
            out.println("</td>");

            //МФУ
            out.println("<td>");
            for (Mfd mfd : mfdList) {
                out.println("<p><a href=\"/config/equipment/addUpdateEquipment?id=" + mfd.getId() +
                        "&redirect=workplace&typeEquipment=printer\">" + mfd + "</a></p>");
            }
            out.println("</td>");

            //принтеры
            out.println("<td>");
            for (Printer printer : printerList) {
                out.println("<p><a href=\"/config/equipment/addUpdateEquipment?id=" + printer.getId() +
                        "&redirect=workplace&typeEquipment=printer\">" + printer + "</a></p>");
            }
            out.println("</td>");

            //Сканеры
            out.println("<td>");
            for (Scanner scanner : scannerList) {
                out.println("<p><a href=\"/config/equipment/addUpdateEquipment?id=" + scanner.getId() +
                        "&redirect=workplace&typeEquipment=printer\">" + scanner + "</a></p>");
            }
            out.println("</td>");

            //ИБП
            out.println("<td>");
            for (Ups ups : upsList) {
                out.println("<p><a href=\"/config/equipment/addUpdateEquipment?id=" + ups.getId() +
                        "&redirect=workplace&typeEquipment=printer\">" + ups + "</a></p>");
            }
            out.println("</td>");

            out.println("<td><a href=\"/config/workplace/deleteWorkplace?id=" + workplace.getId() + "\">Удалить</a></td>");

            out.println("</tr>");
        }
    %>

</table>