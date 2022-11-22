<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.Pages" %>


<h1>Рабочие места</h1>

<%
    String token = (String) request.getAttribute("token");
    Role role = (Role) request.getAttribute("role");

    if (Role.ADMIN.equals(role)) {
        out.println("<a href=\"/" + Pages.addUpdateWorkplace +
                "?token=" + token +
                "&redirect=" + Pages.workplace +
                "\">Добавить рабочее место</a>");
    }
%>

<table>
    <tr>
        <th/>
        <th><h1>Название</h1></th>
        <%
            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.employee +
                    "?token=" + token + "\">Сотрудники</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEmployee +
                        "?redirect=" + Pages.workplace +
                        "&token=" + token + "\">Добавить сотрудника</a></p>");
            }
            out.println("</th>");

            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.computer +
                    "?token=" + token + "\">Компьютеры</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?redirect=" + Pages.workplace +
                        "&typeEquipment=" + TypeEquipment.COMPUTER +
                        "&token=" + token + "\">Добавить компьютер</a></p>");
            }
            out.println("</th>");

            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.monitor +
                    "?token=" + token + "\">Мониторы</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?redirect=" + Pages.workplace +
                        "&typeEquipment=" + TypeEquipment.MONITOR +
                        "&token=" + token + "\">Добавить монитор</a></p>");
            }
            out.println("</th>");

            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.mfd +
                    "?token=" + token + "\">МФУ</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?redirect=" + Pages.workplace +
                        "&typeEquipment=" + TypeEquipment.MFD +
                        "&token=" + token + "\">Добавить МФУ</a></p>");
            }
            out.println("</th>");

            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.printer +
                    "?token=" + token + "\">Принтеры</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?redirect=" + Pages.workplace +
                        "&typeEquipment=" + TypeEquipment.PRINTER +
                        "&token=" + token + "\">Добавить принтер</a></p>");
            }
            out.println("</th>");

            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.scanner +
                    "?token=" + token + "\">Сканеры</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?redirect=" + Pages.workplace +
                        "&typeEquipment=" + TypeEquipment.SCANNER +
                        "&token=" + token + "\">Добавить сканер</a></p>");
            }
            out.println("</th>");

            out.println("<th>");
            out.println("<p><h1><a href=\"/" + Pages.ups +
                    "?token=" + token + "\">ИБП</a></h1></p>");
            if (Role.ADMIN.equals(role)) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?redirect=" + Pages.workplace +
                        "&typeEquipment=" + TypeEquipment.UPS +
                        "&token=" + token + "\">Добавить ИБП</a></p>");
            }
            out.println("</th>");
        %>
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

            out.println("<tr>");

            out.println("<td>" + count + "</td>");
            count++;

            out.println("<td><a href=\"/" + Pages.addUpdateWorkplace +
                    "?id=" + workplace.getId() +
                    "&redirect=" + Pages.workplace +
                    "&token=" + token +
                    "\">" + workplace.getTitle() + "</a></td>");

            out.println("<td>");
            if (!workplace.getEmployeeList().isEmpty()) {
                for (Employee employee : workplace.getEmployeeList()) {
                    out.println("<p><a href=\"/" + Pages.addUpdateEmployee +
                            "?id=" + employee.getId() +
                            "&token=" + token +
                            "&redirect=workplace\">" + employee.getName() + "</a></p>");
                }
            }
            out.println("</td>");

            //компьютеры
            out.println("<td>");
            for (Computer computer : computerList1) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?id=" + computer.getId() +
                        "&token=" + token +
                        "&redirect=" + Pages.workplace +
                        "&typeEquipment=computer\">" + computer + "</a></p>");
            }
            out.println("</td>");

            //мониторы
            out.println("<td>");
            for (Monitor monitor : monitorList) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?id=" + monitor.getId() +
                        "&token=" + token +
                        "&redirect=" + Pages.workplace +
                        "&typeEquipment=monitor\">" + monitor + "</a></p>");
            }
            out.println("</td>");

            //МФУ
            out.println("<td>");
            for (Mfd mfd : mfdList) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?id=" + mfd.getId() +
                        "&token=" + token +
                        "&redirect=" + Pages.workplace +
                        "&typeEquipment=printer\">" + mfd + "</a></p>");
            }
            out.println("</td>");

            //принтеры
            out.println("<td>");
            for (Printer printer : printerList) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?id=" + printer.getId() +
                        "&token=" + token +
                        "&redirect=" + Pages.workplace +
                        "&typeEquipment=printer\">" + printer + "</a></p>");
            }
            out.println("</td>");

            //Сканеры
            out.println("<td>");
            for (Scanner scanner : scannerList) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?id=" + scanner.getId() +
                        "&token=" + token +
                        "&redirect=" + Pages.workplace +
                        "&typeEquipment=printer\">" + scanner + "</a></p>");
            }
            out.println("</td>");

            //ИБП
            out.println("<td>");
            for (Ups ups : upsList) {
                out.println("<p><a href=\"/" + Pages.addUpdateEquipment +
                        "?id=" + ups.getId() +
                        "&token=" + token +
                        "&redirect=" + Pages.workplace +
                        "&typeEquipment=printer\">" + ups + "</a></p>");
            }
            out.println("</td>");

            if (Role.ADMIN.equals(role)) {
                out.println("<td><a href=\"/" + Pages.deleteWorkplacePost +
                        "?id=" + workplace.getId() +
                        "&redirect=" + Pages.workplace +
                        "&token=" + token + "\">Удалить</a></td>");
            }

            out.println("</tr>");
        }
    %>

</table>