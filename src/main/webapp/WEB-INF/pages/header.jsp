<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <tr>
        <td><a href="/workplace?token=${token}" class="button">Рабочие места</a></td>
        <td><a href="/computer?token=${token}" class="button">Компьютеры</a></td>
        <td><a href="/monitor?token=${token}" class="button">Мониторы</a></td>
        <td><a href="/mfd?token=${token}" class="button">МФУ</a> </td>
        <td><a href="/printer?token=${token}" class="button">Принтеры</a></td>
        <td><a href="/scanner?token=${token}" class="button">Сканеры</a> </td>
        <td><a href="/ups?token=${token}" class="button">ИБП</a> </td>
        <td><a href="/employee?token=${token}" class="button">Сотрудники</a></td>
        <td><a href="/accounting1c?token=${token}" class="button">Бухгалтерия</a> </td>
    </tr>
</table>
