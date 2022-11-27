<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.db.domain.components.TypeRam" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="java.util.List" %>
<%@ page import="workplaceManager.db.domain.components.Processor" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="workplaceManager.db.domain.components.Ram" %>
<%@ page import="workplaceManager.Components" %>
<%@ page import="workplaceManager.Parameters" %>

<html>
<head>
    <script type="text/javascript">

        function changeFunc() {
            if (document.getElementById("useRecord").checked) {
                document.getElementById("<%=Components.accounting1CIdSelect%>").disabled = false;
                document.getElementById("<%=Components.accounting1CInventoryNumberInputText%>").disabled = true;
                document.getElementById("<%=Components.accounting1CTitleInputText%>").disabled = true;
                document.getElementById("selectEmployeeId").disabled = true;
            } else if (document.getElementById("addNewRecord").checked) {
                document.getElementById("<%=Components.accounting1CIdSelect%>").disabled = true;
                document.getElementById("<%=Components.accounting1CInventoryNumberInputText%>").disabled = false;
                document.getElementById("<%=Components.accounting1CTitleInputText%>").disabled = false;
                document.getElementById("selectEmployeeId").disabled = false;
            }
        }
    </script>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>

    <%
        Equipment equipment = (Equipment) request.getAttribute(Parameters.equipment);
        String buttonTitle = "Добавить";
        String token = (String) request.getAttribute(Parameters.token);
        Role role = (Role) request.getAttribute(Parameters.role);
        String typeEquipment = (String) request.getAttribute(Parameters.typeEquipment);
        String uid = (equipment != null && equipment.getUid() != null) ? equipment.getUid() : "";
        String manufacturer = (equipment != null && equipment.getManufacturer() != null) ? equipment.getManufacturer() : "";
        String model = (equipment != null && equipment.getModel() != null) ? equipment.getModel() : "";
        String redirect = (String) request.getAttribute(Parameters.redirect);

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
    String error = (String) request.getAttribute(Parameters.error);
    if (error != null && error != "") {
        out.println("<h3>" + error + "</h3>");
    }
    String message = (String) request.getAttribute(Parameters.message);
    if (message != null && message != "") {
        out.println("<h3>" + message + "</h3>");
    }
%>
<form action="<%=url%>" method="post">
    <div class="wrapper_1100">
        <%
            if (equipment != null && equipment.getId() > 0) {
                out.println("<input type=\"hidden\" name=\"" + Parameters.id + "\" value=\"" + equipment.getId() + "\">");
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
                Computer computer = (Computer) request.getAttribute(Parameters.computer);
                out.println("<p>");
                out.println("<label for=\"" + Parameters.ip + "\">IP</label>");
                out.println(String.format("<input type=\"text\" name=\"" + Parameters.ip + "\" id=\"" + Parameters.ip + "\" value=\"%s\"",
                        computer.getIp() == null ? "" : computer.getIp()));

                out.println("<label for=\"" + Parameters.netName + "\">Сетевое имя</label>");
                out.println(String.format("<input type=\"text\" name=\"" + Parameters.netName + "\" id=\"" + Parameters.netName + "\" " + "value=\"%s\">",
                        computer.getNetName() != null ? computer.getNetName() : ""));

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
                List<Workplace> workplaceList = (List<Workplace>) request.getAttribute(Parameters.workplaceList);
                Long workplaceId = (equipment != null && equipment.getWorkplace() != null) ? equipment.getWorkplace().getId() : -1;
            %>
            <label>Рабочее место</label>
            <select name="<%=Parameters.workplaceId%>">
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
            Computer computer = (Computer) request.getAttribute(Parameters.computer);
            out.println("<div class=\"wrapper_1100\">");
            out.println("<h1> Операционная система</h1>");

            out.println("<p>");
            out.println("<label for=\"" + Parameters.OsType + "\">Тип операционной системы</label>");
            out.println("<select name=\"" + Parameters.OsType + "\">");
            for (TypeOS typeOS : TypeOS.values()) {
                if (computer.getOperationSystem() != null && computer.getOperationSystem().getTypeOS() != null &&
                        computer.getOperationSystem().getTypeOS().equals(typeOS)) {
                    out.println(String.format("<option selected value=\"%s\">%s</option>", typeOS, typeOS));
                } else {
                    out.println(String.format("<option value=\"%s\">%s</option>", typeOS, typeOS));
                }
            }
            out.println("</select>");
            out.println("</p>");

            out.println("<p>");
            out.println("<label for=\"" + Parameters.OsVendor + "\">Название</label>");
            out.println(String.format("<input type=\"text\" name=\"" + Parameters.OsVendor + "\" id=\"" + Parameters.OsVendor + "\" " + "value=\"%s\">",
                    computer.getOperationSystem() != null ? computer.getOperationSystem().getVendor() : ""));

            out.println("<label for=\"" + Parameters.OsVersion + "\">Версия</label>");
            out.println(String.format("<input type=\"text\" name=\"" + Parameters.OsVersion + "\" id=\"" + Parameters.OsVersion + "\" " + "value=\"%s\">",
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
                List<Accounting1C> accounting1CList = (List<Accounting1C>) request.getAttribute(Parameters.accounting1CList);
                Long accounting1СId = (equipment != null && equipment.getAccounting1C() != null) ? equipment.getAccounting1C().getId() : -1;
            %>
            <input type="radio" name="<%=Components.accounting1CRadio%>" id="useRecord"
                   value="<%=Parameters.accounting1CUseRecord%>" checked
                   onchange="changeFunc()">Выбрать существующую запись
            <select name="<%=Components.accounting1CIdSelect%>" id="<%=Components.accounting1CIdSelect%>"
                    onchange="changeFunc();">
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
        <p><input type="radio" name="<%=Components.accounting1CRadio%>" id="addNewRecord"
                  value="<%=Parameters.accounting1CAddNewRecord%>" onchange="changeFunc()">Добавить
            новую запись</p>
        <%
            List<Employee> employeeList = (List<Employee>) request.getAttribute(Parameters.employeeList);
        %>
        <p class="align_p">
            <label for="<%=Components.accounting1CInventoryNumberInputText%>">Инвентарный номер</label>
            <input type="text" name="<%=Components.accounting1CInventoryNumberInputText%>"
                   id="<%=Components.accounting1CInventoryNumberInputText%>" disabled>
            <label for="<%=Components.accounting1CTitleInputText%>">Название</label>
            <textarea name="<%=Components.accounting1CTitleInputText%>" id="<%=Components.accounting1CTitleInputText%>"
                      rows="5" disabled></textarea>
            <label for="selectEmployeeId">Материально-ответственное лицо</label>
            <select name="<%=Components.accounting1CEmployeeIdInputText%>" id="selectEmployeeId" disabled>
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
        int countProcessor = 1;
        int countRam = 1;
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computer = (Computer) request.getAttribute(Parameters.computer);
    %>
    <div class="wrapper_1100">
        <p>
        <h1>Конфигурация компьютера
            <%
                if (Role.ADMIN.equals(role)) {
                    out.println("<input type=\"submit\" name=\"" + Components.buttonReadConfigComputer + "\" value=\"Считать конфигурацию компьютера\">");
                }
            %>
        </h1>
        </p>

        <h2>Материнская плата</h2>
        <p>
            <label for="<%=Components.motherboardManufacturer%>">Производитель</label>
            <input type="text" name="<%=Components.motherboardManufacturer%>"
                   id="<%=Components.motherboardManufacturer%>"
                   value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getManufacturer() : ""%>">

            <label for="<%=Components.motherboardModel%>">Модель</label>
            <input type="text" name="<%=Components.motherboardModel%>" id="<%=Components.motherboardModel%>"
                   value="<%=computer.getMotherBoard() != null ? computer.getMotherBoard().getModel() : ""%>">

        </p>

        <h2>Процессор
            <%
                if (Role.ADMIN.equals(role)) {
                    out.println("<input type=\"submit\" name=\"" + Components.buttonAddProcessor + "\" value=\"Добавить процессор\">");
                }
            %>
        </h2>
        <p>
                <%
                countProcessor = 1;
                if (!CollectionUtils.isEmpty(computer.getProcessorList())) {
                    for (Processor processor : computer.getProcessorList()) {
                        String modelName = Components.inputTextProcessorModel + countProcessor;
                        String numberCoreName = Components.inputTextProcessorNumberOfCores + countProcessor;
                        String frequencyName = Components.inputTextProcessorFrequency + countProcessor;
                        String socketName = Components.inputTextProcessorSocket + countProcessor;
                        String buttonDeleteProcessor = Components.buttonDeleteProcessor+countProcessor;
            %>
        <p>
            <label for="<%=modelName%>">Модель</label>
            <input type="text" name="<%=modelName%>" id="<%=modelName%>"
                   value="<%=processor.getModel() != null ? processor.getModel() : ""%>">

            <label for="<%=numberCoreName%>">Количество ядер</label>
            <input type="text" name="<%=numberCoreName%>" id="<%=numberCoreName%>"
                   value="<%=processor.getNumberOfCores() != null ? processor.getNumberOfCores() : ""%>">

            <label for="<%=frequencyName%>">Частота</label>
            <input type="text" name="<%=frequencyName%>" id="<%=frequencyName%>"
                   value="<%=processor.getFrequency() != null ? processor.getFrequency() : ""%>">

            <label for="<%=socketName%>">Сокет</label>
            <input type="text" name="<%=socketName%>" id="<%=socketName%>"
                   value="<%=processor.getSocket() != null ? processor.getSocket() : ""%>">
            <%
                if (Role.ADMIN.equals(role)) {
                    out.println("<input type=\"submit\" name=\"" + buttonDeleteProcessor + "\" value=\"Удалить\">");
                }
            %>
        </p>
        <%
                    countProcessor++;
                }
            }
        %>
        </p>

        <h2>Оперативная память
            <%
                if (Role.ADMIN.equals(role)) {
                    //out.println("<input type=\"submit\" name=\"buttonAddProcessor\" value=\"Добавить процессор\">");
                }
            %>
        </h2>

        <p>
            <!--<label for="selectTypeRam">Тип оперативной памяти</label>
            <select name="selectTypeRam" id="selectTypeRam">-->
            <%
                /*for (TypeRam typeRam : TypeRam.values()) {
                    if (computer.getMotherBoard() != null && typeRam.equals(computer.getMotherBoard().getTypeRam())) {
                        out.println("<option selected value=\"" + typeRam + "\">" + typeRam + "</option>");
                    } else {
                        out.println("<option value=\"" + typeRam + "\">" + typeRam + "</option>");
                    }
                }*/
            %>
            <!-- </select>-->
        </p>
    </div>
    <%
        }
    %>

    <div align="center">
        <p>
            <%
                if (Role.ADMIN.equals(role)) {
                    out.println("<input type=\"submit\" name=\"" + Components.buttonSave + "\" value=\"" + buttonTitle + "\">");
                }
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <input type="hidden" name="<%=Parameters.token%>" value="<%=token%>">
            <input type="hidden" name="<%=Parameters.typeEquipment%>" value="<%=typeEquipment%>">
            <input type="hidden" name="<%=Parameters.countProcessor%>" value="<%=countProcessor%>">

            <a href="/<%=redirect%>?<%=Parameters.token%>=<%=token%>" class="button">Назад</a>
            <%
                if (Role.ADMIN.equals(role) && (equipment != null && equipment.getId() > 0)) {
                    Long id;
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) request.getAttribute(Parameters.computer);
                        id = computer.getId();
                    } else {
                        id = equipment.getId();
                    }

                    out.println(String.format("<td><a href=\"/" + Pages.deleteEquipmentPost +
                                    "?" + Parameters.id + "=%s" +
                                    "&" + Parameters.token + "=%s" +
                                    "&" + Parameters.redirect + "=%s" +
                                    "&" + Parameters.typeEquipment + "=%s\">Удалить</a> </td>",
                            id, token, redirect, typeEquipment));
                }
            %>
        </p>
    </div>
</form>

</body>
</html>
