<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Бухгалтерия</h1>
<a href="/config/accounting1c/addUpdateAccounting1C?redirect=accounting1c">Добавить оборудование в бухгалтерию</a>

<table>
    <tr>
        <th>ID</th>
        <th>Инвентарный номер</th>
        <th>Название</th>
        <th>Материально-отвественное лицо</th>
        <th>Оборудование</th>
    </tr>

    <%
        List<Accounting1C> accounting1CList = (List<Accounting1C>) request.getAttribute("accounting1CList");
        int count = 1;
        for (Accounting1C accounting1C : accounting1CList) {
            out.println("<tr>");

            out.println("<td>" + count + "</td>");
            count++;

            out.println("<td>");
            out.println("<a href=\"/config/accounting1c/addUpdateAccounting1C?" +
                    "id=" + accounting1C.getId() +
                    "&redirect=accounting1c\">" + accounting1C.getInventoryNumber() + "</a>");
            out.println("</td>");

            out.println("<td>");
            out.println("<a href=\"/config/accounting1c/addUpdateAccounting1C?" +
                    "id=" + accounting1C.getId() +
                    "&redirect=accounting1c\">" + accounting1C.getTitle() + "</a>");
            out.println("</td>");

            if (accounting1C.getEmployee() != null) {
                out.println("<td>" + accounting1C.getEmployee().getName() + "</td>");
            } else {
                out.println("<td/>");
            }

            if (!accounting1C.getEquipmentList().isEmpty()) {
                out.println("<td>");
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

                    out.println("<p>");
                    out.println("<a href=\"/config/equipment/addUpdateEquipment" +
                            "?id=" + equipment.getId() +
                            "&redirect=accounting1c" +
                            "&typeEquipment=" + typeEquipment + "\">" +
                            equipment + "</a>");
                    out.println("</p>");
                }

                out.println("</td>");
            } else {
                out.println("<td/>");
            }

            out.println("<td><a href=\"/config/accounting1c/deleteAccounting1C?id=" + accounting1C.getId() + "\">Удалить</a></td>");

            out.println("</tr>");
        }
    %>
</table>
