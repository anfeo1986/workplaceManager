<%@ page import="workplaceManager.db.domain.Computer" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="workplaceManager.db.domain.components.Ram" %>
<%@ page import="workplaceManager.db.domain.components.HardDrive" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${title}</h1>

<a href="/config/equipment/addUpdateEquipment?redirect=${page}&typeEquipment=${typeEquipment}">Добавить </a>

<table>
    <tr>
        <th>id</th>
        <th>${title}</th>
        <th>Процессор</th>
        <th>Материнская плата</th>
        <th>Оперативная память</th>
        <th>Жесткие диски</th>
        <th>Видеокарта</th>
        <th>Рабочее место</th>
        <th>Бухгалтерия</th>
    </tr>
    <%
        List<Computer> computerList = (List<Computer>) request.getAttribute("equipmentList");
        String redirect = (String) request.getAttribute("page");
        String typeEquipment = (String) request.getAttribute("typeEquipment");
        int count = 1;
        for(Computer computer : computerList) {
            out.println("<tr>");

            out.println(String.format("<td>%s</td>", count));
            count++;

            out.println(String.format("<td><a href=\"/config/equipment/addUpdateEquipment?id=%s&redirect=%s&typeEquipment=%s\">%s</a></td>",
                    computer.getId(), redirect, typeEquipment, computer));

            out.println(String.format("<td>%s</td>", computer.getProcessor() != null ? computer.getProcessor() : ""));

            out.println(String.format("<td>%s</td>", computer.getMotherBoard() != null ? computer.getMotherBoard() : ""));

            String ramStr = "";
            if(!CollectionUtils.isEmpty(computer.getRamList())) {
                int countRam = 1;
                for(Ram ram : computer.getRamList()) {
                    ramStr += String.format("%s. %s", countRam, ram);
                    countRam++;
                }
            }
            out.println(String.format("<td>%s</td>", ramStr));

            String hardDriveStr = "";
            if(!CollectionUtils.isEmpty(computer.getHardDriveList())) {
                int countHardDrive = 1;
                for(HardDrive hardDrive : computer.getHardDriveList()) {
                    hardDriveStr += String.format("%s. %s", countHardDrive, hardDrive);
                    countHardDrive++;
                }
            }
            out.println(String.format("<td>%s</td>", hardDriveStr));

            out.println(String.format("<td>%s</td>", computer.getVideoCard() != null ? computer.getVideoCard() : ""));

            if(computer.getWorkplace() != null) {
                out.println(String.format("<td><a href=\"/config/workplace/addUpdateWorkplace?id=%s&redirect=%s&typeEquipment=%s\">%s</a></td>",
                        computer.getWorkplace().getId(), redirect, typeEquipment, computer.getWorkplace().getTitle()));
            } else {
                out.println("<td/>");
            }

            if(computer.getAccounting1C() != null) {
                out.println(String.format("<td><a href=\"/config/accounting1c/addUpdateAccounting1C?id=%s&redirect=%s&typeEquipment=%s\">%s</a></td>",
                        computer.getAccounting1C().getId(), redirect, typeEquipment, computer.getAccounting1C()));
            } else {
                out.println("<td/>");
            }

            out.println(String.format("<td><a href=\"/config/equipment/deleteEquipment?id=%s&redirect=%s&typeEquipment=%s\">Удалить</a> </td>",
                    computer.getId(), redirect, typeEquipment));

            out.println("</tr>");
        }
    %>
</table>