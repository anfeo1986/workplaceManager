<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.db.domain.components.TypeRam" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <script type="text/javascript">

        function changeFunc() {
            if (document.getElementById("useRecord").checked) {
                document.getElementById("selectAccounting1CId").disabled = false;
                document.getElementById("accounting1CInventoryNumber").disabled = true;
                document.getElementById("accounting1CTitle").disabled = true;
                document.getElementById("selectEmployeeId").disabled = true;
            } else if (document.getElementById("addNewRecord").checked) {
                document.getElementById("selectAccounting1CId").disabled = true;
                document.getElementById("accounting1CInventoryNumber").disabled = false;
                document.getElementById("accounting1CTitle").disabled = false;
                document.getElementById("selectEmployeeId").disabled = false;
            }
        }
    </script>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>

    <%
        Equipment equipment = (Equipment) request.getAttribute("equipment");
        String buttonTitle = "Добавить";
        String token = (String) request.getAttribute("token");
        Role role = (Role) request.getAttribute("role");
        String typeEquipment = (String) request.getAttribute("typeEquipment");
        String uid = (equipment != null && equipment.getUid() != null) ? equipment.getUid() : "";
        String manufacturer = (equipment != null && equipment.getManufacturer() != null) ? equipment.getManufacturer() : "";
        String model = (equipment != null && equipment.getModel() != null) ? equipment.getModel() : "";

        String url = "";
        if (equipment != null && equipment.getId() > 0) {
            out.println("<title>Редактирование</title>");
            url = "/" + Pages.updateEquipmentPost;
            buttonTitle = "Редактировать";
        } else {
            out.println("<title>Добавление</title>");
            url = "/" + Pages.addEquipmentPost;
        }
    %>
</head>
<body>
<%
    String error = (String) request.getAttribute("error");
    if (error != null && error != "") {
        out.println("<h3>" + error + "</h3>");
    }
    String message = (String) request.getAttribute("message");
    if (message != null && message != "") {
        out.println("<h3>" + message + "</h3>");
    }
%>
<form action="<%=url%>" method="post">
    <div class="wrapper_1100">
        <%
            if (equipment != null && equipment.getId() > 0) {
                out.println("<input type=\"hidden\" name=\"id\" value=\"" + equipment.getId() + "\">");
            }
        %>
        <p align="center">
        <h1>Основная информация</h1></p>
        <p>
            <label for="uid">UID</label>
            <input type="text" name="uid" id="uid" value="<%=uid%>">
        </p>
        <%
            if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                Computer computer = (Computer) request.getAttribute("computer");
                out.println("<p>");
                out.println("<label for=\"ip\">IP</label>");
                out.println(String.format("<input type=\"text\" name=\"ip\" id=\"ip\" value=\"%s\"",
                        computer.getIp() == null ? "" : computer.getIp()));
                out.println("</p>");
            }
        %>
        <p>
            <label for="manufacturer">Производитель</label>
            <input type="text" name="manufacturer" id="manufacturer" value="<%=manufacturer%>">
            <label for="model">Модель</label>
            <input type="text" name="model" id="model" value="<%=model%>">
        </p>
        <p>
            <%
                List<Workplace> workplaceList = (List<Workplace>) request.getAttribute("workplaceList");
                Long workplaceId = (equipment != null && equipment.getWorkplace() != null) ? equipment.getWorkplace().getId() : -1;
            %>
            <label>Рабочее место</label>
            <select name="workplace_id">
                <option value="-1"/>
                <%
                    for (Workplace workplace : workplaceList) {
                        if (workplace.getId() == workplaceId) {
                            out.println("<option selected value=\"" + workplace.getId() + "\">" + workplace.getTitle() + "</option>");
                        } else {
                            out.println("<option value=\"" + workplace.getId() + "\">" + workplace.getTitle() + "</option>");
                        }
                    }
                %>
            </select>
        </p>
    </div>

    <%
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computer = (Computer) request.getAttribute("computer");
            out.println("<div class=\"wrapper_1100\">");
            out.println("<h1> Операционная система</h1>");

            out.println("<p>");
            out.println("<label for=\"type_operationsystem\">Тип операционной системы</label>");
            out.println("<select name=\"type_operationsystem\">");
            for (TypeOS typeOS : TypeOS.values()) {
                if (computer.getOperationSystem() != null && computer.getOperationSystem().getTypeOS().equals(typeOS)) {
                    out.println(String.format("<option selected value=\"%s\">%s</option>", typeOS, typeOS));
                } else {
                    out.println(String.format("<option value=\"%s\">%s</option>", typeOS, typeOS));
                }
            }
            out.println("</select>");
            out.println("</p>");

            out.println("<p>");
            out.println("<label for=\"vendor_operationsystem\">Название</label>");
            out.println(String.format("<input type=\"text\" name=\"vendor_operationsystem\" id=\"vendor_operationsystem\" " + "value=\"%s\">",
                    computer.getOperationSystem() != null ? computer.getOperationSystem().getVendor() : ""));

            out.println("<label for=\"version_operationsystem\">Версия</label>");
            out.println(String.format("<input type=\"text\" name=\"version_operationsystem\" id=\"version_operationsystem\" " + "value=\"%s\">",
                    computer.getOperationSystem() != null ? computer.getOperationSystem().getVersion() : ""));
            out.println("</p>");
            out.println("</div>");
        }
    %>
    <div class="wrapper_1100">
        <p>
        <h1>Бухгалтерия</h1></p>
        <p>
            <%
                List<Accounting1C> accounting1CList = (List<Accounting1C>) request.getAttribute("accounting1CList");
                Long accounting1СId = (equipment != null && equipment.getAccounting1C() != null) ? equipment.getAccounting1C().getId() : -1;
            %>
            <input type="radio" name="accounting1CRadio" id="useRecord" value="useRecord" checked
                   onchange="changeFunc()">Выбрать существующую запись
            <select name="selectAccounting1CId" id="selectAccounting1CId" onchange="changeFunc();">
                <%
                    if (accounting1СId == -1) {
                        out.println("<option selected value=\"-1\"/>");
                    } else {
                        out.println("<option value=\"-1\"/>");
                    }
                    for (Accounting1C accounting1C : accounting1CList) {
                        if (accounting1C.getId() == accounting1СId && accounting1СId != -1) {
                            out.println("<option selected value=\"" + accounting1C.getId() + "\">" + accounting1C + "</option>");
                        } else {
                            out.println("<option value=\"" + accounting1C.getId() + "\">" + accounting1C + "</option>");
                        }
                    }
                %>
            </select>
        </p>
        <p class="align_p">
        <p><input type="radio" name="accounting1CRadio" id="addNewRecord" value="addNewRecord" onchange="changeFunc()">Добавить
            новую запись</p>
        <%
            List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
        %>
        <p class="align_p">
            <label for="accounting1CInventoryNumber">Инвентарный номер</label>
            <input type="text" name="accounting1CInventoryNumber" id="accounting1CInventoryNumber" disabled>
            <label for="accounting1CTitle">Название</label>
            <textarea name="accounting1CTitle" id="accounting1CTitle" rows="5" disabled></textarea>
            <label for="selectEmployeeId">Материально-ответственное лицо</label>
            <select name="employeeId" id="selectEmployeeId" disabled>
                <option value="-1"/>
                <%
                    for (Employee employee : employeeList) {
                        out.println("<option value=\"" + employee.getId() + "\">" + employee.getName() + "</option>");
                    }
                %>
            </select>
        </p>
        </p>
    </div>
    <%
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computer = (Computer) request.getAttribute("computer");
    %>
    <div class="wrapper_1100">
        <p>
        <h1>Материнская плата</h1></p>
        <p>
            <label for="motherboard_manufacturer">Производитель</label>
            <input type="text" name="motherboard_manufacturer" id="motherboard_manufacturer"
                   value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getManufacturer() : ""%>">

            <label for="motherboard_model">Модель</label>
            <input type="text" name="motherboard_model" id="motherboard_model"
                   value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getModel() : ""%>">

            <label for="motherboard_socket">Сокет</label>
            <input type="text" name="motherboard_socket" id="motherboard_socket"
                   value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getSocket() : ""%>">
        </p>
        <p>
            <label for="selectTypeRam">Тип оперативной памяти</label>
            <select name="selectTypeRam" id="selectTypeRam">
                <%
                    for (TypeRam typeRam : TypeRam.values()) {
                        if (computer.getMotherBoard() != null && typeRam.equals(computer.getMotherBoard().getTypeRam())) {
                            out.println("<option selected value=\"" + typeRam + "\">" + typeRam + "</option>");
                        } else {
                            out.println("<option value=\"" + typeRam + "\">" + typeRam + "</option>");
                        }
                    }
                %>
            </select>

            <label for="motherboard_ram_frequency">Частота оперативной памяти</label>
            "<input type="text" name="motherboard_ram_frequency" id="motherboard_ram_frequency"
                    value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getRamFrequency() : ""%>">

            <label for="motherboard_ram_max_amount">Максимальный объем оперативной памяти</label>
            <input type="text" name="motherboard_ram_max_amount" id="motherboard_ram_max_amount"
                   value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getRamMaxAmount() : ""%>">
        </p>
    </div>
    <%
        }
    %>

    <div align="center">
        <p>
            <%
                if (Role.ADMIN.equals(role)) {
                    out.println("<input type=\"submit\" value=\"" + buttonTitle + "\">");
                }
                String redirect = (String) request.getAttribute("redirect");
            %>
            <input type="hidden" name="redirect" value="<%=redirect%>">
            <input type="hidden" name="token" value="<%=token%>">
            <input type="hidden" name="typeEquipment" value="<%=typeEquipment%>">
            <a href="/<%=redirect%>?token=<%=token%>" class="button">Назад</a>
        </p>
    </div>
</form>

</body>
</html>
