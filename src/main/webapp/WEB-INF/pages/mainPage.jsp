<%@ page import="workplaceManager.db.domain.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>


    <c:if test="${page == 'computer'}">
        <title>Компьютеры</title>
    </c:if>
    <c:if test="${page == 'employee'}">
        <title>Сотрудники</title>
    </c:if>
    <c:if test="${page == 'workplace'}">
        <title>Рабочие места</title>
    </c:if>
    <c:if test="${page == 'accounting1c'}">
        <title>Бухгалтерия</title>
    </c:if>
    <c:if test="${page == 'monitor'}">
        <title>Мониторы</title>
    </c:if>
    <c:if test="${page == 'printer'}">
        <title>Принтеры</title>
    </c:if>
    <c:if test="${page == 'scanner'}">
        <title>Сканеры</title>
    </c:if>
    <c:if test="${page == 'mfd'}">
        <title>МФУ</title>
    </c:if>
    <c:if test="${page == 'ups'}">
        <title>ИБП</title>
    </c:if>
    <c:if test="${page == 'virtualMachine'}">
        <title>Виртуальные машины</title>
    </c:if>

    <script type="text/javascript">
        document.addEventListener('visibilitychange', () => {
            console.log(document.visibilityState);
            if (document.visibilityState === 'visible') {
                // страница видима
                //console.log('Вернулся');
                location.reload();
            } else {
                // страница скрыта
                //console.log('Ушел');
            }
        });
        function close_window() {
            close();
        }
    </script>

</head>
<!--<body background='graphics/background.jpg'>-->
<body>
<%
    String baseUrl = (String) request.getAttribute(Parameters.baseUrl);
    //String token = (String) request.getAttribute(Parameters.token);

%>

<section class="sticky">
    <%@include file='header.jsp' %>
</section>

<div>
    <c:if test="${page == 'computer'}">

        <%@include file="computerTable.jsp" %>

    </c:if>
    <c:if test="${page == 'employee'}">

        <%@include file='employeeTable.jsp' %>

    </c:if>
    <c:if test="${page == 'workplace'}">

        <%@include file='workplaceTable.jsp' %>

    </c:if>
    <c:if test="${page == 'accounting1c'}">

        <%@include file="accounting1cTable.jsp" %>

    </c:if>
    <c:if test="${page == 'monitor'}">

        <%@include file="equipmentTable.jsp" %>

    </c:if>
    <c:if test="${page == 'printer'}">

        <%@include file="equipmentTable.jsp" %>

    </c:if>
    <c:if test="${page == 'scanner'}">

        <%@include file="equipmentTable.jsp" %>

    </c:if>
    <c:if test="${page == 'mfd'}">

        <%@include file="equipmentTable.jsp" %>

    </c:if>
    <c:if test="${page == 'ups'}">
        <%@include file="equipmentTable.jsp" %>
    </c:if>

    <c:if test="${page == 'virtualMachine'}">
        <%@include file="virtualMachineTable.jsp" %>
    </c:if>

</div>

</body>
</html>