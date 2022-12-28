<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="workplaceManager.db.domain.components.*" %>
<%@ page import="workplaceManager.*" %>
<%@ page import="java.util.ArrayList" %>

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
        };

        function close_window() {
            close();
        };
    </script>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>

    <%
        Equipment equipment = request.getAttribute(Parameters.equipment) != null ?
                (Equipment) request.getAttribute(Parameters.equipment) : null;
        String buttonTitle = "Добавить";
        //String token = (String) request.getAttribute(Parameters.token);
        Role role = request.getSession().getAttribute(Parameters.role) != null ?
                (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;
        String typeEquipment = request.getAttribute(Parameters.typeEquipment) != null ?
                (String) request.getAttribute(Parameters.typeEquipment) : "";
        String uid = (equipment != null && equipment.getUid() != null) ?
                equipment.getUid() : "";
        String manufacturer = (equipment != null && equipment.getManufacturer() != null) ?
                equipment.getManufacturer() : "";
        String model = (equipment != null && equipment.getModel() != null) ?
                equipment.getModel() : "";
        String redirect = request.getAttribute(Parameters.redirect) != null ?
                (String) request.getAttribute(Parameters.redirect) : "";
        String baseUrl = request.getAttribute(Parameters.baseUrl) != null ?
                (String) request.getAttribute(Parameters.baseUrl) : "";
        Long workplaceIdFromRequest = request.getParameter(Parameters.workplaceId) != null ?
                Long.parseLong(request.getParameter(Parameters.workplaceId)) : -1L;
        Boolean isClose = request.getAttribute(Parameters.closeWindow) != null ?
                (Boolean) request.getAttribute(Parameters.closeWindow) : false;
        List<Workplace> workplaceList = request.getAttribute(Parameters.workplaceList) != null ?
                (List<Workplace>) request.getAttribute(Parameters.workplaceList) : new ArrayList<>();
        List<Accounting1C> accounting1CList = request.getAttribute(Parameters.accounting1CList) != null ?
                (List<Accounting1C>) request.getAttribute(Parameters.accounting1CList) : new ArrayList<>();
        List<Employee> employeeList = request.getAttribute(Parameters.employeeList) != null ?
                (List<Employee>) request.getAttribute(Parameters.employeeList) : new ArrayList<>();

        String url = baseUrl;
        String title = "";
        if (equipment != null && equipment.getId() > 0) {
            title = "Редактирование ";
            url += Pages.updateEquipmentPost;
            buttonTitle = "Редактировать";
        } else {
            title = "Добавление ";
            url += Pages.addEquipmentPost;
        }
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            title += "компьютера";
        } else if (TypeEquipment.MONITOR.equals(typeEquipment)) {
            title += "монитора";
        } else if (TypeEquipment.PRINTER.equals(typeEquipment)) {
            title += "принтера";
        } else if (TypeEquipment.SCANNER.equals(typeEquipment)) {
            title += "сканера";
        } else if (TypeEquipment.MFD.equals(typeEquipment)) {
            title += "МФУ";
        } else if (TypeEquipment.UPS.equals(typeEquipment)) {
            title += "ИБП";
        }
    %>
    <title><%=title%>
    </title>

</head>

<body>
<%
    if (isClose) {
%>
<script>
    close_window();
</script>
<%
    }
%>
<section class="sticky">
    <%@include file='/WEB-INF/pages/header.jsp' %>
</section>
<h1 align="center"><%=title%>
</h1>
<%
    String error = (String) request.getAttribute(Parameters.error);
    if (error != null && error != "") {
%>
<h3><%=error%>
</h3>
<%
    }
    String message = (String) request.getAttribute(Parameters.message);
    if (message != null && message != "") {
%>
<h3><%=message%>
</h3>
<%
    }
%>
<form action="<%=url%>" method="post">
    <div class="wrapper_1100">
        <%
            if (equipment != null && equipment.getId() > 0) {
        %>
        <input type="hidden" name="<%=Parameters.id%>" value="<%=equipment.getId()%>">
        <%
            }
        %>
        <p align="center">
        <h1>Основная информация</h1></p>
        <p>
            <label for="uid">UID</label>
            <input type="text" name="uid" id="uid" autofocus value="<%=uid%>">
            <label for="manufacturer">Производитель</label>
            <input type="text" name="manufacturer" id="manufacturer" value="<%=manufacturer%>">
            <label for="model">Модель</label>
            <input type="text" name="model" id="model" value="<%=model%>">
        </p>
        <%
            if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                Computer computer = (Computer) request.getAttribute(Parameters.computer);
        %>
        <p>
            <label for="<%=Parameters.ip%>">IP</label>
            <input type="text" name="<%=Parameters.ip%>" id="<%=Parameters.ip%>"
                   value="<%=computer.getIp() == null ? "" : computer.getIp()%>">

            <label for="<%=Parameters.netName%>">Сетевое имя</label>
            <input type="text" name="<%=Parameters.netName%>" id="<%=Parameters.netName%>"
                   value="<%=computer.getNetName() != null ? computer.getNetName() : ""%>">

        </p>
        <%
            }
        %>
        <p>
            <%
                Long workplaceId = (equipment != null && equipment.getWorkplace() != null) ? equipment.getWorkplace().getId() : -1;
            %>
            <label>Рабочее место</label>
            <select name="<%=Parameters.workplaceId%>">
                <option value="-1"/>
                <%
                    for (Workplace workplace : workplaceList) {
                        if (workplace.getId() == workplaceId || workplace.getId() == workplaceIdFromRequest) {
                %>
                <option selected value="<%=workplace.getId()%>">
                    <%=workplace.getTitleHtml()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=workplace.getId()%>"><%=workplace.getTitleHtml()%>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </p>
        <p class="align_p">
            <label for="<%=Parameters.comment%>">Комментарий</label>
            <textarea name="<%=Parameters.comment%>" id="<%=Parameters.comment%>"
                      rows="5"
                      cols="100"><%=(equipment != null && equipment.getComment() != null) ? equipment.getCommentHtml() : ""%></textarea>
        </p>
    </div>

    <%
        int countVirtualMachine = 1;
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computer = (Computer) request.getAttribute(Parameters.computer);
    %>
    <div class="wrapper_1100">
        <h1> Операционная система</h1>

        <p>
            <label for="<%=Parameters.OsType%>">Тип операционной системы</label>
            <select name="<%=Parameters.OsType%>">
                <%
                    for (TypeOS typeOS : TypeOS.values()) {
                        if (computer.getOperationSystem() != null && computer.getOperationSystem().getTypeOS() != null &&
                                computer.getOperationSystem().getTypeOS().equals(typeOS)) {
                %>
                <option selected value="<%=typeOS%>"><%=typeOS%>
                </option>
                <%
                } else {
                %>
                <option value="<%=typeOS%>"><%=typeOS%>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </p>

        <p>
            <label for="<%=Parameters.OsVendor%>">Название</label>
            <input type="text" name="<%=Parameters.OsVendor%>" id="<%=Parameters.OsVendor%>"
                   value="<%=(computer.getOperationSystem() != null && computer.getOperationSystem().getVendor() != null)
                   ? computer.getOperationSystem().getVendor() : ""%>">

            <label for="<%=Parameters.OsVersion%>">Версия</label>
            <input type="text" name="<%=Parameters.OsVersion%>" id="<%=Parameters.OsVersion%>"
                   value="<%=(computer.getOperationSystem() != null && computer.getOperationSystem().getVersion() != null)
                   ? computer.getOperationSystem().getVersion() : ""%>">

            <label for="<%=Parameters.OSArchitecture%>">Архитектура</label>
            <input type="text" name="<%=Parameters.OSArchitecture%>" id="<%=Parameters.OSArchitecture%>"
                   value="<%=(computer.getOperationSystem() != null && computer.getOperationSystem().getOSArchitecture() != null)
                   ? computer.getOperationSystem().getOSArchitecture() : ""%>">
        </p>
    </div>
    <div class="wrapper_1100">
        <h1> Виртуальные машины
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=Components.buttonAddVirtualMachine%>"
                   value="Добавить">
            <%
                }
            %>
        </h1>

        <%
            if (computer != null && computer.getVirtualMachineList() == null) {
                for (VirtualMachine virtualMachine : computer.getVirtualMachineList()) {
                    String ipVirtualMachineName = Parameters.ipVirtualMachine + countVirtualMachine;
                    String osTypeVirtualMachineName = Parameters.OsTypeVirtualMachine + countVirtualMachine;
                    String osVendorVirtualMachineName = Parameters.OsVendorVirtualMachine + countVirtualMachine;
                    String osVersionVirtualMachineName = Parameters.OsVersionVirtualMachine + countVirtualMachine;
                    String virtualMachineIdName = Parameters.idVirtualMachine + countVirtualMachine;
                    String virtualMachineButtonDelete = Parameters.virtualMachineButtonDelete + countVirtualMachine;

        %>
        <p>
            <label for="<%=ipVirtualMachineName%>">IP</label>
            <input type="text" name="<%=ipVirtualMachineName%>" id="<%=ipVirtualMachineName%>"
                   value="<%=virtualMachine.getIp() != null ? virtualMachine.getIp() : ""%>">

            <label for="<%=osTypeVirtualMachineName%>">Тип ОС</label>
            <select name="<%=osTypeVirtualMachineName%>">
                <%
                    for (TypeOS typeOS : TypeOS.values()) {
                        if (virtualMachine.getTypeOS() != null && virtualMachine.getTypeOS().equals(typeOS)) {
                %>
                <option selected value="<%=typeOS%>"><%=typeOS%>
                </option>
                <%
                } else {
                %>
                <option value="<%=typeOS%>"><%=typeOS%>
                </option>
                <%
                        }
                    }
                %>
            </select>

            <label for="<%=osVendorVirtualMachineName%>">Название ОС</label>
            <input type="text" name="<%=osVendorVirtualMachineName%>" id="<%=osVendorVirtualMachineName%>"
                   value="<%=virtualMachine.getVendor() != null ? virtualMachine.getVendor() : ""%>">

            <label for="<%=osVersionVirtualMachineName%>">Версия ОС</label>
            <input type="text" name="<%=osVersionVirtualMachineName%>" id="<%=osVersionVirtualMachineName%>"
                   value="<%=virtualMachine.getVersion() != null ? virtualMachine.getVersion() : ""%>">

            <input type="hidden" name="<%=virtualMachineIdName%>" value="<%=virtualMachine.getId()%>">

            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=virtualMachineButtonDelete%>" value="Удалить">
            <%
                }
            %>
        </p>
        <%

                    countVirtualMachine++;
                }
            }
        %>
    </div>
    <%
        }
    %>
    <div class="wrapper_1100">
        <p>
        <h1>Бухгалтерия</h1></p>
        <p>
            <%
                Long accounting1СId = (equipment != null && equipment.getAccounting1C() != null) ? equipment.getAccounting1C().getId() : -1;
            %>
            <input type="radio" name="<%=Components.accounting1CRadio%>" id="useRecord"
                   value="<%=Parameters.accounting1CUseRecord%>" checked
                   onchange="changeFunc()">Выбрать существующую запись
            <select name="<%=Components.accounting1CIdSelect%>" id="<%=Components.accounting1CIdSelect%>"
                    onchange="changeFunc();">
                <%
                    if (accounting1СId == -1) {
                %>
                <option selected value="-1"/>
                <%
                } else {
                %>
                <option value="-1"/>
                <%
                    }
                    for (Accounting1C accounting1C : accounting1CList) {
                        if (accounting1C.getId() == accounting1СId && accounting1СId != -1) {
                %>
                <option selected value="<%=accounting1C.getId()%>"><%=accounting1C%>
                </option>
                <%
                } else {
                %>
                <option value="<%=accounting1C.getId()%>"><%=accounting1C%>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </p>
        <p class="align_p">
        <p><input type="radio" name="<%=Components.accounting1CRadio%>" id="addNewRecord"
                  value="<%=Parameters.accounting1CAddNewRecord%>" onchange="changeFunc()">Добавить
            новую запись</p>
        <p class="align_p">
            <label for="<%=Components.accounting1CInventoryNumberInputText%>">Инвентарный номер</label>
            <input type="text" name="<%=Components.accounting1CInventoryNumberInputText%>"
                   id="<%=Components.accounting1CInventoryNumberInputText%>" disabled>
            <label for="<%=Components.accounting1CTitleInputText%>">Название</label>
            <textarea name="<%=Components.accounting1CTitleInputText%>"
                      id="<%=Components.accounting1CTitleInputText%>"
                      rows="5" disabled></textarea>
            <label for="selectEmployeeId">Материально-ответственное лицо</label>
            <select name="<%=Components.accounting1CEmployeeIdInputText%>" id="selectEmployeeId" disabled>
                <option value="-1"/>
                <%
                    for (Employee employee : employeeList) {
                %>
                <option value="<%=employee.getId()%>"><%=employee.getName()%>
                </option>
                <%
                    }
                %>
            </select>
        </p>
        </p>
    </div>
    <%
        int countProcessor = 1;
        int countRam = 1;
        int countVideoCard = 1;
        int countHardDrive = 1;
        if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
            Computer computer = (Computer) request.getAttribute(Parameters.computer);
    %>
    <div class="wrapper_1100">
        <p>
        <h1>Конфигурация компьютера
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=Components.buttonAddVirtualMachine%>"
                   value="Считать конфигурацию компьютера">
            <%
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
            %>
            <input type="submit" name="<%=Components.buttonAddProcessor%>" value="Добавить">
            <%
                }
            %>
        </h2>
        <p>
                <%
                countProcessor = 1;
                if (!CollectionUtils.isEmpty(computer.getProcessorList())) {
                    for (Processor processor : computer.getProcessorList()) {
                        String modelName = Components.processorModelInputText + countProcessor;
                        String numberCoreName = Components.processorNumberOfCoresInputText + countProcessor;
                        String frequencyName = Components.processorFrequencyInputText + countProcessor;
                        String socketName = Components.processorSocketInputText + countProcessor;
                        String processorIdName = Components.processorIdHiddenText + countProcessor;
                        String buttonDeleteProcessor = Components.buttonDeleteProcessor+countProcessor;
            %>
        <p>
            <label for="<%=modelName%>">Модель</label>
            <input type="text" name="<%=modelName%>" id="<%=modelName%>" size="35"
                   value="<%=processor.getModel() != null ? processor.getModel() : ""%>">

            <label for="<%=numberCoreName%>">Количество ядер</label>
            <input type="text" name="<%=numberCoreName%>" id="<%=numberCoreName%>" size="4"
                   value="<%=processor.getNumberOfCores() != null ? processor.getNumberOfCores() : ""%>">

            <label for="<%=frequencyName%>">Частота</label>
            <input type="text" name="<%=frequencyName%>" id="<%=frequencyName%>" size="8"
                   value="<%=processor.getFrequency() != null ? processor.getFrequency() : ""%>">

            <label for="<%=socketName%>">Сокет</label>
            <input type="text" name="<%=socketName%>" id="<%=socketName%>" size="8"
                   value="<%=processor.getSocket() != null ? processor.getSocket() : ""%>">

            <input type="hidden" name="<%=processorIdName%>" value="<%=processor.getId()%>">
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=buttonDeleteProcessor%>" value="Удалить">
            <%
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
            %>
            <input type="submit" name="<%=Components.buttonAddRam%>" value="Добавить">
            <%
                }
            %>
        </h2>
        <p>
                <%
                countRam = 1;
                if (!CollectionUtils.isEmpty(computer.getRamList())) {
                    for (Ram ram : computer.getRamList()) {
                        String ramModelName = Components.ramModelInputText + countRam;
                        String ramTypeRamName = Components.ramTypeRamSelect + countRam;
                        String ramAmountName = Components.ramAmountInputText + countRam;
                        String ramFrequencyName = Components.ramFrequencyInputText + countRam;
                        String ramDeviceLocatorName = Components.ramDeviceLocatorInputText + countRam;
                        String ramIdName = Components.ramIdHiddenText + countRam;
                        String buttonDeleteRam = Components.buttonDeleteRam+countRam;
            %>
        <p>
            <label for="<%=ramModelName%>">Модель</label>
            <input type="text" name="<%=ramModelName%>" id="<%=ramModelName%>"
                   value="<%=ram.getModel() != null ? ram.getModel() : ""%>">

            <label for="<%=ramTypeRamName%>">Тип</label>
            <select name="<%=ramTypeRamName%>" id="<%=ramTypeRamName%>">
                <%
                    for (TypeRam typeRam : TypeRam.values()) {
                        if (ram.getTypeRam() != null && typeRam.equals(ram.getTypeRam())) {
                %>
                <option selected value="<%=typeRam%>"><%=typeRam%>
                </option>
                <%
                } else {
                %>
                <option value="<%=typeRam%>"><%=typeRam%>
                </option>
                <%
                        }
                    }
                %>
            </select>

            <label for="<%=ramAmountName%>">Объем</label>
            <input type="text" name="<%=ramAmountName%>" id="<%=ramAmountName%>" size="6"
                   value="<%=ram.getAmount() != null ? ram.getAmount() : ""%>">

            <label for="<%=ramFrequencyName%>">Частота</label>
            <input type="text" name="<%=ramFrequencyName%>" id="<%=ramFrequencyName%>" size="6"
                   value="<%=ram.getFrequency() != null ? ram.getFrequency() : ""%>">

            <label for="<%=ramDeviceLocatorName%>">Разъём</label>
            <input type="text" name="<%=ramDeviceLocatorName%>" id="<%=ramDeviceLocatorName%>" size="12"
                   value="<%=ram.getDeviceLocator() != null ? ram.getDeviceLocator() : ""%>">

            <input type="hidden" name="<%=ramIdName%>" value="<%=ram.getId()%>">
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=buttonDeleteRam%>" value="Удалить">
            <%
                }
            %>
        </p>
        <%
                    countRam++;
                }
            }
        %>
        </p>

        <h2>Видеокарты
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=Components.buttonAddVideoCard%>" value="Добавить">
            <%
                }
            %>
        </h2>
        <p>
                <%
                countVideoCard = 1;
                if (!CollectionUtils.isEmpty(computer.getVideoCardList())) {
                    for (VideoCard videoCard : computer.getVideoCardList()) {
                        String videocardModelName = Components.videoCardModelInputText + countVideoCard;
                        String videoCardIdName = Components.videoCardIdHiddenText + countVideoCard;
                        String buttonDelete = Components.buttonDeleteVideoCard + countVideoCard;
            %>
        <p>
            <label for="<%=videocardModelName%>">Модель</label>
            <input type="text" name="<%=videocardModelName%>" id="<%=videocardModelName%>"
                   value="<%=videoCard.getModel() != null ? videoCard.getModel() : ""%>">

            <input type="hidden" name="<%=videoCardIdName%>" value="<%=videoCard.getId()%>">
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=buttonDelete%>" value="Удалить">
            <%
                }
            %>
        </p>
        <%
                    countVideoCard++;
                }
            }
        %>
        </p>

        <h2>Жёсткие диски
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=Components.buttonAddHardDrive%>" value="Добавить">
            <%
                }
            %>
        </h2>
        <p>
                <%
                countHardDrive = 1;
                if (!CollectionUtils.isEmpty(computer.getHardDriveList())) {
                    for (HardDrive hardDrive : computer.getHardDriveList()) {
                        String hardDriveModelName = Components.hardDriveModelInputText + countHardDrive;
                        String hardDriveSizeName = Components.hardDriveSizeInputText + countHardDrive;
                        String hardDriveIdName = Components.hardDriveIdHiddenText + countHardDrive;
                        String buttonDelete = Components.buttonDeleteHardDrive + countHardDrive;
            %>
        <p>
            <label for="<%=hardDriveModelName%>">Модель</label>
            <input type="text" name="<%=hardDriveModelName%>" id="<%=hardDriveModelName%>"
                   value="<%=hardDrive.getModel() != null ? hardDrive.getModel() : ""%>">

            <label for="<%=hardDriveSizeName%>">Объем</label>
            <input type="text" name="<%=hardDriveSizeName%>" id="<%=hardDriveSizeName%>"
                   value="<%=hardDrive.getSize() != null ? hardDrive.getSize() : ""%>">

            <input type="hidden" name="<%=hardDriveIdName%>" value="<%=hardDrive.getId()%>">
            <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=buttonDelete%>" value="Удалить">
            <%
                }
            %>
        </p>
        <%
                    countHardDrive++;
                }
            }
        %>
        </p>


    </div>
    <%
        }
    %>

    <div align="center">
        <p>
                <%
                if (Role.ADMIN.equals(role)) {
            %>
            <input type="submit" name="<%=Components.buttonSave%>" value="<%=buttonTitle%>">
                <%
                }
            %>
            <input type="hidden" name="<%=Parameters.redirect%>" value="<%=redirect%>">
            <input type="hidden" name="<%=Parameters.typeEquipment%>" value="<%=typeEquipment%>">
            <input type="hidden" name="<%=Parameters.countProcessor%>" value="<%=countProcessor%>">
            <input type="hidden" name="<%=Parameters.countVirtualMachine%>" value="<%=countVirtualMachine%>">
            <input type="hidden" name="<%=Parameters.countRam%>" value="<%=countRam%>">
            <input type="hidden" name="<%=Parameters.countVideoCard%>" value="<%=countVideoCard%>">
            <input type="hidden" name="<%=Parameters.countHardDrive%>" value="<%=countHardDrive%>">

            <a onclick="close_window(); return false;" class="button">Назад</a>
                <%
                if (Role.ADMIN.equals(role) && (equipment != null && equipment.getId() > 0)) {
                    Long id;
                    if (TypeEquipment.COMPUTER.equals(typeEquipment)) {
                        Computer computer = (Computer) request.getAttribute(Parameters.computer);
                        id = computer.getId();
                    } else {
                        id = equipment.getId();
                    }
            %>
        <td>
            <a href="<%=baseUrl + Pages.deleteEquipmentPost%>?<%=Parameters.id%>=<%=id%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                Удалить</a>
        </td>
        <%
            }
        %>
        </p>
    </div>
</form>

</body>
</html>
