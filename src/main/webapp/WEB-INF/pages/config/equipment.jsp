<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.db.domain.components.TypeRam" %>

<html>
<head>
    <script type="text/javascript">

        function changeFunc() {
            if (document.getElementById("noRecord").checked) {
                document.getElementById("selectAccounting1CId").disabled = true;
                document.getElementById("accounting1CInventoryNumber").disabled = true;
                document.getElementById("accounting1CTitle").disabled = true;
                document.getElementById("selectEmployeeId").disabled = true;
            } else if (document.getElementById("useRecord").checked) {
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

    <c:if test="${equipment.id > 0}">
        <title>Редактирование</title>
        <c:url var="url" value="/config/equipment/updateEquipmentPost"/>
        <c:set var="equipmentId" value="${equipment.id}"/>
        <c:set var="buttonTitle" value="Сохранить"/>
    </c:if>
    <c:if test="${equipment.id <= 0}">
        <title>Добавление</title>
        <c:url var="url" value="/config/equipment/addEquipmentPost"/>
        <c:set var="equipmentId" value="0"/>
        <c:set var="buttonTitle" value="Добавить"/>
    </c:if>

    <c:if test="${empty equipment.uid}">
        <c:set var="uid" value=""/>
    </c:if>
    <c:if test="${!empty equipment.uid}">
        <c:set var="uid" value="${equipment.uid}"/>
    </c:if>

    <c:if test="${empty equipment.manufacturer}">
        <c:set var="manufacturer" value=""/>
    </c:if>
    <c:if test="${!empty equipment.manufacturer}">
        <c:set var="manufacturer" value="${equipment.manufacturer}"/>
    </c:if>

    <c:if test="${empty equipment.model}">
        <c:set var="model" value=""/>
    </c:if>
    <c:if test="${!empty equipment.model}">
        <c:set var="model" value="${equipment.model}"/>
    </c:if>

    <c:if test="${empty equipment.workplace}">
        <c:set var="workplaceId" value=""/>
    </c:if>
    <c:if test="${!empty equipment.workplace}">
        <c:set var="workplaceId" value="${equipment.workplace.id}"/>
    </c:if>

    <c:if test="${empty equipment.accounting1C}">
        <c:set var="accounting1СId" value=""/>
    </c:if>
    <c:if test="${!empty equipment.accounting1C}">
        <c:set var="accounting1СId" value="${equipment.accounting1C.id}"/>
    </c:if>

</head>
<body>
<c:if test="${!empty error}">
    <h3><c:out value="${error}"/></h3>
</c:if>
<c:if test="${!empty message}">
    <h3><c:out value="${message}"/></h3>
</c:if>

<form action="${url}" method="post">
    <c:if test="${equipmentId > 0}">
        <input type="hidden" name="id" value="${equipmentId}">
    </c:if>

    <div class="wrapper_1100">
        <p align="center">
        <h1>Основная информация</h1></p>
        <p>
            <label for="uid">UID</label>
            <input type="text" name="uid" id="uid" value="${uid}">
        </p>

        <p>
            <label for="manufacturer">Производитель</label>
            <input type="text" name="manufacturer" id="manufacturer" value="${manufacturer}">
            <label for="model">Модель</label>
            <input type="text" name="model" id="model" value="${model}">
        </p>

        <p>
            <label>Рабочее место</label>
            <select name="workplace_id">
                <option value="-1"/>
                <c:forEach var="workplace" items="${workplaceList}">
                    <c:if test="${workplaceId == workplace.id}">
                        <option selected value="${workplace.id}">${workplace.title}</option>
                    </c:if>
                    <c:if test="${workplaceId != workplace.id}">
                        <option value="${workplace.id}">${workplace.title}</option>
                    </c:if>
                </c:forEach>
            </select>
        </p>
    </div>

    <div class="wrapper_1100">
        <p>
        <h1>Бухгалтерия</h1></p>

        <p>
            <input type="radio" name="accounting1CRadio" id="noRecord" value="noRecord" onchange="changeFunc()">
                Не привязывать к бухгалтерии
        </p>

        <p>
            <input type="radio" name="accounting1CRadio" id="useRecord" value="useRecord" checked
                   onchange="changeFunc()">
            Использовать существующую запись
            <select name="selectAccounting1CId" id="selectAccounting1CId" onchange="changeFunc();">
                <c:forEach var="accounting1C" items="${accounting1CList}">
                    <c:if test="${accounting1СId == accounting1C.id}">
                        <option selected value="${accounting1C.id}">${accounting1C}</option>
                    </c:if>
                    <c:if test="${accounting1СId != accounting1C.id}">
                        <option value="${accounting1C.id}">${accounting1C}</option>
                    </c:if>
                </c:forEach>
            </select>
        </p>
        <p class="align_p">
        <p><input type="radio" name="accounting1CRadio" id="addNewRecord" value="addNewRecord" onchange="changeFunc()">
            Добавить новую запись</p>
        <p class="align_p">
            <label for="accounting1CInventoryNumber">Инвентарный номер</label>
            <input type="text" name="accounting1CInventoryNumber" id="accounting1CInventoryNumber" disabled>

            <label for="accounting1CTitle">Название</label>
            <textarea name="accounting1CTitle" id="accounting1CTitle" rows="5" disabled></textarea>


            <label for="selectEmployeeId">Материально-ответственное лицо</label>
            <select name="employeeId" id="selectEmployeeId" disabled>
                <option value="-1"/>
                <c:forEach var="employee" items="${employeeList}">
                    <option value="${employee.id}">${employee.name}</option>
                </c:forEach>"
            </select>
        </p>
        </p>
    </div>

    <c:if test="${typeEquipment == 'computer'}">

        <div class="wrapper_1100">
            <p>
            <h1>Материнская плата</h1></p>
            <%
                out.println("<p>");
                Computer computer = (Computer) request.getAttribute("computer");
                out.println("<label for=\"motherboard_manufacturer\">Производитель</label>");
                out.println(String.format("<input type=\"text\" name=\"motherboard_manufacturer\" id=\"motherboard_manufacturer\" " + "value=\"%s\">",
                        computer.getMotherBoard() != null ? computer.getMotherBoard().getManufacturer() : ""));

                out.println("<label for=\"motherboard_model\">Модель</label>");
                out.println(String.format("<input type=\"text\" name=\"motherboard_model\" id=\"motherboard_model\" value=\"%s\">",
                        computer.getMotherBoard() != null ? computer.getMotherBoard().getModel() : ""));

                out.println("<label for=\"motherboard_socket\">Сокет</label>");
                out.println(String.format("<input type=\"text\" name=\"motherboard_socket\" id=\"motherboard_socket\" value=\"%s\">",
                        computer.getMotherBoard() != null ? computer.getMotherBoard().getSocket() : ""));

                out.println("</p>");
                out.println("<p>");

                out.println("<label for=\"selectTypeRam\">Тип оперативной памяти</label>");
                out.println("<select name=\"selectTypeRam\" id=\"selectTypeRam\">");
                for (TypeRam typeRam : TypeRam.values()) {
                    if (computer.getMotherBoard() != null && typeRam.equals(computer.getMotherBoard().getTypeRam())) {
                        out.println("<option selected value=\"" + typeRam + "\">" + typeRam + "</option>");
                    } else {
                        out.println("<option value=\"" + typeRam + "\">" + typeRam + "</option>");
                    }
                }
                out.println("</select>");

                out.println("<label for=\"motherboard_ram_frequency\">Частота оперативной памяти</label>");
                out.println(String.format("<input type=\"text\" name=\"motherboard_ram_frequency\" id=\"motherboard_ram_frequency\" value=\"%s\">",
                        computer.getMotherBoard() != null ? computer.getMotherBoard().getRamFrequency() : ""));

                out.println("<label for=\"motherboard_ram_max_amount\">Максимальный объем оперативной памяти</label>");
                out.println(String.format("<input type=\"text\" name=\"motherboard_ram_max_amount\" id=\"motherboard_ram_max_amount\" value=\"%s\">",
                        computer.getMotherBoard() != null ? computer.getMotherBoard().getRamMaxAmount() : ""));
                out.println("</p>");
            %>
        </div>
    </c:if>

    <div align="center">
        <p>
            <input type="submit" value="${buttonTitle}">
            <input type="hidden" name="redirect" value="${redirect}">
            <input type="hidden" name="typeEquipment" value="${typeEquipment}">
            <a href="/${redirect}" class="button">Назад</a>
        </p>
    </div>
</form>

</body>
</html>
