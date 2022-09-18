<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <script type="text/javascript">

        function changeFunc() {
            if(document.getElementById("noRecord").checked) {
                document.getElementById("selectAccounting1CId").disabled=true;
                document.getElementById("accounting1CInventoryNumber").disabled=true;
                document.getElementById("accounting1CTitle").disabled=true;
                document.getElementById("selectEmployeeId").disabled=true;
            } else if(document.getElementById("useRecord").checked) {
                document.getElementById("selectAccounting1CId").disabled=false;
                document.getElementById("accounting1CInventoryNumber").disabled=true;
                document.getElementById("accounting1CTitle").disabled=true;
                document.getElementById("selectEmployeeId").disabled=true;
            } else if(document.getElementById("addNewRecord").checked) {
                document.getElementById("selectAccounting1CId").disabled=true;
                document.getElementById("accounting1CInventoryNumber").disabled=false;
                document.getElementById("accounting1CTitle").disabled=false;
                document.getElementById("selectEmployeeId").disabled=false;
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

    <p>
        <label for="uid">UID</label>
        <input type="text" name="uid" id="uid" value="${uid}">
    </p>

    <p>
        <label for="manufacturer">Производитель</label>
        <input type="text" name="manufacturer" id="manufacturer" value="${manufacturer}">
    </p>

    <p>
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

    <p>
        <input type="radio" name="accounting1CRadio" id="noRecord" value="noRecord" onchange="changeFunc()"> Не привязывать к бухгалтерии
    </p>

    <p>
        <input type="radio" name="accounting1CRadio" id="useRecord" value="useRecord" checked onchange="changeFunc()"> Использовать существующую запись
        <select name="selectAccounting1CId" id="selectAccounting1CId" onchange="changeFunc();">
            <option value="-1"/>
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
    <p><input type="radio" name="accounting1CRadio" id="addNewRecord" value="addNewRecord" onchange="changeFunc()"> Добавить новую запись</p>
    <p>
        <label for="accounting1CInventoryNumber">Инвентарный номер</label>
        <input type="text" name="accounting1CInventoryNumber" id="accounting1CInventoryNumber" disabled>

        <label for="accounting1CTitle">Название</label>
        <textarea name="accounting1CTitle" id="accounting1CTitle" disabled></textarea>

        <label for="selectEmployeeId">Материально-ответственное лицо</label>
        <select name="employeeId" id="selectEmployeeId" disabled>
            <option value="-1"/>
            <c:forEach var="employee" items="${employeeList}">
                <option value="${employee.id}">${employee.name}</option>
            </c:forEach>"
        </select>
    </p>

    <p>
        <input type="submit" value="${buttonTitle}">
        <input type="hidden" name="redirect" value="${redirect}">
        <input type="hidden" name="typeEquipment" value="${typeEquipment}">
        <a href="/${redirect}" class="button">Назад</a>
    </p>
</form>

</body>
</html>
