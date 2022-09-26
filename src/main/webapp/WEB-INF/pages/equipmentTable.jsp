<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${title}</h1>

<a href="/config/equipment/addUpdateEquipment?redirect=${page}&typeEquipment=${typeEquipment}">Добавить </a>

<table>
    <tr>
        <th>id</th>
        <th>${title}</th>
        <th>Рабочее место</th>
        <th>Бухгалтерия</th>
    </tr>
    <c:set var="count" value="1"/>
    <c:forEach var="equipment" items="${equipmentList}">
        <tr>
            <td>${count}</td>
            <c:set var="count" value="${count+1}"/>


            <td><a href="/config/equipment/addUpdateEquipment?id=${equipment.id}&redirect=${page}&typeEquipment=${typeEquipment}">${equipment}</a></td>

            <c:if test="${!empty equipment.workplace}">
                <td>
                    <a href="/config/workplace/addUpdateWorkplace?id=${equipment.workplace.id}&redirect=${page}&typeEquipment=${typeEquipment}">
                        ${equipment.workplace.title}
                    </a>
                </td>
            </c:if>
            <c:if test="${empty equipment.workplace}">
                <td/>
            </c:if>

            <c:if test="${!empty equipment.accounting1C}">
                <td>
                    <a href="/config/accounting1c/addUpdateAccounting1C?id=${equipment.accounting1C.id}&redirect=${page}&typeEquipment=${typeEquipment}">
                            ${equipment.accounting1C}
                    </a>
                </td>
            </c:if>
            <c:if test="${empty equipment.accounting1C}">
                <td/>
            </c:if>

            <td><a href="/config/equipment/deleteEquipment?id=${equipment.id}&redirect=${page}&typeEquipment=${typeEquipment}">Удалить</a> </td>
        </tr>
    </c:forEach>
</table>