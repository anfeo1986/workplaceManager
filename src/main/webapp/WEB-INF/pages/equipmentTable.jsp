<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${title}</h1>

<%
    String token = (String) request.getAttribute("token");
    Role role = (Role) request.getAttribute("role");
    String typeEquipment = (String) request.getAttribute("typeEquipment");
    String redirect = (String) request.getAttribute("page");
    String title = (String) request.getAttribute("title");
    List<Equipment> equipmentList = (List<Equipment>) request.getAttribute("equipmentList");

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
        out.println("<a href=\"/" + Pages.addUpdateEquipment +
                "?token=" + token +
                "&redirect=" + redirect +
                "&typeEquipment=" + typeEquipment +
                "\">Добавить " + typeEquipmentStr + "</a>");
    }
%>
<!--<a href="/config/equipment/addUpdateEquipment?redirect=${page}&typeEquipment=${typeEquipment}">Добавить </a>-->

<table>
    <tr>
        <%
            out.println("<th><h1>id</h1></th>");
            out.println("<th><h1>" + title + "</h1></th>");
            out.println("<th><h1>Рабочее место</h1></th>");
            out.println("<th><h1>Бухгалтерия</h1></th>");
        %>
    </tr>

    <%
        int count = 1;
        for (Equipment equipment : equipmentList) {
            out.println("<tr>");

            out.println("<td>" + count + "</td>");
            count++;

            out.println("<td>");
            out.println("<a href=\"/" + Pages.addUpdateEquipment +
                    "?id=" + equipment.getId() +
                    "&token=" + token +
                    "&redirect=" + redirect +
                    "&typeEquipment=" + typeEquipment + "\">" + equipment + "</a>");
            out.println("</td>");

            if (equipment.getWorkplace() != null) {
                out.println("<td>");
                out.println("<a href=\"/" + Pages.addUpdateWorkplace +
                        "?id=" + equipment.getWorkplace().getId() +
                        "&token=" + token +
                        "&redirect=" + redirect +
                        "&typeEquipment=" + typeEquipment + "\">" + equipment.getWorkplace().getTitle() + "</a>");
                out.println("</td>");
            } else {
                out.println("<td/>");
            }

            if (equipment.getAccounting1C() != null) {
                out.println("<td>");
                out.println("<a href=\"/" + Pages.addUpdateAccounting1C +
                        "?id=" + equipment.getAccounting1C().getId() +
                        "&token=" + token +
                        "&redirect=" + redirect +
                        "&typeEquipment=" + typeEquipment + "\">" + equipment.getAccounting1C() + "</a>");
                out.println("</td>");
            } else {
                out.println("<td/>");
            }

            if (Role.ADMIN.equals(role)) {
                out.println("<td>");
                out.println("<a href=\"/" + Pages.deleteEquipmentPost +
                        "?id=" + equipment.getId() +
                        "&token=" + token +
                        "&redirect=" + redirect +
                        "&typeEquipment=" + typeEquipment + "\">Удалить</a>");
                out.println("</td>");
            }

            out.println("</tr>");
        }
    %>
</table>