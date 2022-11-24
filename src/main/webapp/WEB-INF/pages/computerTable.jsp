<%@ page import="workplaceManager.db.domain.Computer" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="workplaceManager.db.domain.components.Ram" %>
<%@ page import="workplaceManager.db.domain.components.HardDrive" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${title}</h1>

<%
    String token = (String) request.getAttribute("token");
    Role role = (Role) request.getAttribute("role");
    String typeEquipment = (String) request.getAttribute("typeEquipment");
    String redirect = (String) request.getAttribute("page");
    String title = (String) request.getAttribute("title");
    List<Computer> computerList = (List<Computer>) request.getAttribute("equipmentList");

    if (Role.ADMIN.equals(role)) {
        out.println("<a href=\"/" + Pages.addUpdateEquipment +
                "?redirect=" + redirect +
                "&token=" + token +
                "&typeEquipment=" + typeEquipment + "\">Добавить компьютер</a>");
    }
%>

<table>
    <tr>
        <%
            out.println("<th><h1>id</h1></th>");
            out.println("<th><h1>" + title + "</h1></th>");
            out.println("<th><h1>ОС</h1></th>");
            out.println("<th><h1>Процессор</h1></th>");
            out.println("<th><h1>Материнская плата</h1></th>");
            out.println("<th><h1>Оперативная память</h1></th>");
            out.println("<th><h1>Жесткие диски</h1></th>");
            out.println("<th><h1>Видеокарта</h1></th>");
            out.println("<th><h1>Рабочее место</h1></th>");
            out.println("<th><h1>Бухгалтерия</h1></th>");
        %>
    </tr>
    <%
        int count = 1;
        for (Computer computer : computerList) {
            out.println("<tr>");

            out.println(String.format("<td>%s</td>", count));
            count++;

            out.println(String.format("<td><a href=\"/" + Pages.addUpdateEquipment +
                            "?id=%s" +
                            "&token=%s" +
                            "&redirect=%s" +
                            "&typeEquipment=%s\">%s</a></td>",
                    computer.getId(), token, redirect, typeEquipment, computer));

            out.println(String.format("<td>%s</td>", computer.getOperationSystem() != null ? computer.getOperationSystem() : ""));

            out.println(String.format("<td>%s</td>", computer.getProcessor() != null ? computer.getProcessor() : ""));

            out.println(String.format("<td>%s</td>", computer.getMotherBoard() != null ? computer.getMotherBoard() : ""));

            String ramStr = "";
            if (!CollectionUtils.isEmpty(computer.getRamList())) {
                int countRam = 1;
                for (Ram ram : computer.getRamList()) {
                    ramStr += String.format("%s. %s", countRam, ram);
                    countRam++;
                }
            }
            out.println(String.format("<td>%s</td>", ramStr));

            String hardDriveStr = "";
            if (!CollectionUtils.isEmpty(computer.getHardDriveList())) {
                int countHardDrive = 1;
                for (HardDrive hardDrive : computer.getHardDriveList()) {
                    hardDriveStr += String.format("%s. %s", countHardDrive, hardDrive);
                    countHardDrive++;
                }
            }
            out.println(String.format("<td>%s</td>", hardDriveStr));

            out.println(String.format("<td>%s</td>", computer.getVideoCard() != null ? computer.getVideoCard() : ""));

            if (computer.getWorkplace() != null) {
                out.println(String.format("<td><a href=\"/" + Pages.addUpdateWorkplace +
                                "?id=%s" +
                                "&token=%s" +
                                "&redirect=%s" +
                                "&typeEquipment=%s\">%s</a></td>",
                        computer.getWorkplace().getId(), token, redirect, typeEquipment, computer.getWorkplace().getTitle()));
            } else {
                out.println("<td/>");
            }

            if (computer.getAccounting1C() != null) {
                out.println(String.format("<td><a href=\"/" + Pages.addUpdateAccounting1C +
                                "?id=%s" +
                                "&token=%s" +
                                "&redirect=%s" +
                                "&typeEquipment=%s\">%s</a></td>",
                        computer.getAccounting1C().getId(), token, redirect, typeEquipment, computer.getAccounting1C()));
            } else {
                out.println("<td/>");
            }

            if (Role.ADMIN.equals(role)) {
                out.println(String.format("<td><a href=\"/" + Pages.deleteEquipmentPost +
                                "?id=%s" +
                                "&token=%s" +
                                "&redirect=%s" +
                                "&typeEquipment=%s\">Удалить</a> </td>",
                        computer.getId(), token, redirect, typeEquipment));
            }
            out.println("</tr>");
        }
    %>
</table>