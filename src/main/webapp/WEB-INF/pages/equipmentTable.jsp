<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${title}</h1>

<a href="/config/equipment/addUpdateEquipment?redirect=${page}&typeEquipment=${typeEquipment}">Добавить </a>

<table>
    <tr>
        <th>id</th>
        <th>UID</th>
        <th>Модель</th>
        <th>Рабочее место</th>
        <th>Бухгалтерия</th>
    </tr>
    <c:forEach var="equipment" items="${equipmentList}">
        <tr>
            <td>${equipment.id}</td>
            <td><a href="/config/equipment/addUpdateEquipment?id=${equipment.id}&redirect=${page}&typeEquipment=${typeEquipment}">${equipment.uid}</a></td>
            <td><a href="/config/equipment/addUpdateEquipment?id=${equipment.id}&redirect=${page}&typeEquipment=${typeEquipment}">${equipment.manufacturer} ${equipment.model}</a></td>
            <td/>
            <td/>
            <td><a href="/config/equipment/deleteEquipment?id=${equipment.id}&redirect=${page}&typeEquipment=${typeEquipment}">Удалить</a> </td>
        </tr>
    </c:forEach>
</table>