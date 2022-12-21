<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="wrapper" align="center">
    <form action="#" method="get">
        <p>
            <b>Сортировка:</b>
            <select name="select" id="select">
                <option value="1">По инвентарному номеру</option>
                <option value="2">По названию</option>
                <option value="3">По материально-ответственному лицу</option>
            </select>
            <b>Поиск:</b>
            <input type="text" name="text" id="text" size="30" value="">
            <input type="submit" value="Применить">
        </p>
    </form>
</div>