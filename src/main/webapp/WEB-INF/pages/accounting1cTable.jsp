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
    <c:forEach var="accounting1C" items="${accounting1CList}">
        <tr>
            <td>${accounting1C.id}</td>
            <td><a href="/config/accounting1c/addUpdateAccounting1C?d=${accounting1C.id}&redirect=accounting1c">${accounting1C.inventoryNumber}</a></td>
            <td><a href="/config/accounting1c/addUpdateAccounting1C?d=${accounting1C.id}&redirect=accounting1c">${accounting1C.title}</a></td>
            <c:if test="${!empty accounting1C.employee}">
                <td>${accounting1C.employee.name}</td>
            </c:if>
            <c:if test="${empty accounting1C.employee}">
                <td/>
            </c:if>
            <td>
                <c:if test="${!empty accounting1C.equipmentList}">
                    <c:forEach var="euqipment" items="${accounting1C.equipmentList}">
                        <p>${equipment}</p>
                    </c:forEach>
                </c:if>
            </td>

        </tr>
    </c:forEach>
</table>
