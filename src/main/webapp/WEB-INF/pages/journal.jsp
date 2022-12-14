<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.*" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.db.domain.Scanner" %>
<%@ page import="org.springframework.security.core.parameters.P" %>
<%@ page import="java.text.SimpleDateFormat" %><%--
  Created by IntelliJ IDEA.
  User: feoktistov
  Date: 07.12.2022
  Time: 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>
    <!--<meta http-equiv="Refresh" content="5"/>-->
    <title>Журнал</title>
    <script type="text/javascript">
        function changeTypeObject() {
            document.getElementById('<%=Components.objectForFilterJournal%>').selectedIndex = 0;
            document.getElementById('<%=Components.eventForFilterJournal%>').selectedIndex = 0;
            document.getElementById('<%=Components.parameterForFilterJournal%>').selectedIndex = 0;

            document.getElementById('filterForm').submit();
        }
    </script>

    <!--<style>
        select {
            width: 200px;
        }
    </style>-->
</head>
<body>

<%
    List<Journal> journalList = new ArrayList<>();
    journalList = (List<Journal>) request.getAttribute(Parameters.journalList);

    String typeObjectStr = (String) request.getAttribute(Parameters.journalFilterTypeObject);
    TypeObject typeObject = null;
    if (typeObjectStr != null && !typeObjectStr.isEmpty()) {
        typeObject = TypeObject.valueOf(typeObjectStr);
    }

    Long objectId = request.getAttribute(Parameters.journalFilterObjectId) != null ?
            (Long) request.getAttribute(Parameters.journalFilterObjectId) : -1;
    SortedMap<String, Long> objectIdList = (SortedMap<String, Long>) request.getAttribute(Parameters.journalObjectIdListForFilter);

    String typeEventStr = (String) request.getAttribute(Parameters.journalFilterTypeEvent);
    TypeEvent typeEvent = null;
    if (typeEventStr != null && !typeEventStr.isEmpty()) {
        typeEvent = TypeEvent.valueOf(typeEventStr);
    }
    List<TypeEvent> typeEventList = (List<TypeEvent>) request.getAttribute(Parameters.journalTypeEventListForFilter);

    String typeParameterStr = (String) request.getAttribute(Parameters.journalFilterTypeParameter);
    TypeParameter typeParameter = null;
    if (typeParameterStr != null && !typeParameterStr.isEmpty()) {
        typeParameter = TypeParameter.valueOf(typeParameterStr);
    }
    List<TypeParameter> typeParameterList = (List<TypeParameter>) request.getAttribute(Parameters.journalParametersListForFilter);

    Users user = (Users) request.getAttribute(Parameters.journalFilterUser);
    List<Users> usersList = (List<Users>) request.getAttribute(Parameters.journalUsersListForFilter);

    String stateObjectForFilterStr = (String) request.getAttribute(Parameters.journalFilterStateObject);
    StateObject stateObjectForFilter = null;
    if (stateObjectForFilterStr != null && !stateObjectForFilterStr.isEmpty()) {
        stateObjectForFilter = StateObject.valueOf(stateObjectForFilterStr);
    }
    List<StateObject> stateObjectList = (List<StateObject>) request.getAttribute(Parameters.journalStateObjectListForFilter);

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatWithTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    Date dateStart = (Date) request.getAttribute(Parameters.journalDateStartForFilter);
    String dateStartStr = "";
    if (dateStart == null) {
        dateStart = new Date();
        dateStartStr = format.format(dateStart) + "T00:00";
    } else {
        dateStartStr = formatWithTime.format(dateStart);
    }
    Date dateEnd = (Date) request.getAttribute(Parameters.journalDateEndForFilter);
    String dateEndStr = "";
    if (dateEnd == null) {
        dateEnd = new Date();
        dateEndStr = format.format(dateEnd) + "T23:59";
    } else {
        dateEndStr = formatWithTime.format(dateEnd);
    }
%>

<form action="<%=Pages.journal%>" method="get" id="filterForm">
    <div align="center" vertical-align="middle">
        <h1>Фильтры</h1>

        <div style="float:left; width:20%;vertical-align: center;">
            <p align="center">
                <b>Начальная дата:</b><input id="<%=Components.dateStartForFilterJournal%>"
                                             type="datetime-local"
                                             name="<%=Components.dateStartForFilterJournal%>"
                                             value="<%=dateStartStr%>"
                                             onchange="changeTypeObject()">
            </p>
            <p>
                <b>Конечная дата:</b><input id="<%=Components.dateEndForFilterJournal%>"
                                            type="datetime-local"
                                            name="<%=Components.dateEndForFilterJournal%>"
                                            value="<%=dateEndStr%>"
                                            onchange="changeTypeObject()">
            </p>
        </div>

        <div style="float:left; width:60%;vertical-align: center;">
            <p align="center">
                <!--Тип объекта-->
                <label for="<%=Components.typeObjectForFilterJournal%>"><b>Тип объекта</b></label>
                <select name="<%=Components.typeObjectForFilterJournal%>"
                        id="<%=Components.typeObjectForFilterJournal%>"
                        onchange="changeTypeObject()">
                    <%
                        if (typeObject == null) {
                    %>
                    <option selected value="">Все</option>
                    <%
                    } else {
                    %>
                    <option value="">Все</option>
                    <%
                        }
                    %>
                    <%
                        for (TypeObject type : TypeObject.values()) {
                            if (type.equals(typeObject)) {
                    %>
                    <option selected value="<%=type.name()%>"><%=type.getTitle()%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=type.name()%>"><%=type.getTitle()%>
                    </option>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                </select>
                <!--Тип объекта.Конец-->

                <!--Тип события-->
                <label for="<%=Components.eventForFilterJournal%>"><b>Событие</b></label>
                <select name="<%=Components.eventForFilterJournal%>" id="<%=Components.eventForFilterJournal%>"
                        onchange="document.getElementById('filterForm').submit()">
                    <%
                        if (typeEvent == null) {
                    %>
                    <option selected value="">Все</option>
                    <%
                    } else {
                    %>
                    <option value="">Все</option>
                    <%
                        }
                    %>
                    <%
                        for (TypeEvent type : typeEventList) {
                            if (typeEvent != null && typeEvent.equals(type)) {
                    %>
                    <option selected value="<%=type.name()%>"><%=type.getTitle()%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=type.name()%>"><%=type.getTitle()%>
                    </option>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                </select>
                <!--Тип события.Конец-->

                <!--Параметр-->
                <label for="<%=Components.parameterForFilterJournal%>"><b>Параметр</b></label>
                <select name="<%=Components.parameterForFilterJournal%>" id="<%=Components.parameterForFilterJournal%>"
                        onchange="document.getElementById('filterForm').submit()">
                    <%
                        if (typeParameter == null) {
                    %>
                    <option selected value="">Все</option>
                    <%
                    } else {
                    %>
                    <option value="">Все</option>
                    <%
                        }
                    %>
                    <%
                        for (TypeParameter type : typeParameterList) {
                            if (typeParameter != null && typeParameter.equals(type)) {
                    %>
                    <option selected value="<%=type.name()%>"><%=type.getTitle()%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=type.name()%>"><%=type.getTitle()%>
                    </option>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                </select>
                <!--Параметр.Конец-->

            </p>
            <p align="center">
                <!--Объект-->
                <label for="<%=Components.objectForFilterJournal%>"><b>Объект</b></label>
                <select name="<%=Components.objectForFilterJournal%>" id="<%=Components.objectForFilterJournal%>"
                        onchange="document.getElementById('filterForm').submit()">
                    <%
                        if (objectId == null || objectId <= 0) {
                    %>
                    <option selected value="-1">Все</option>
                    <%
                    } else {
                    %>
                    <option value="-1">Все</option>
                    <%
                        }
                    %>
                    <%
                        for (String objectStr : objectIdList.keySet()) {
                            Long id = objectIdList.get(objectStr);
                            if (objectId.equals(id)) {
                    %>
                    <option selected value="<%=id%>"><%=objectStr%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=id%>"><%=objectStr%>
                    </option>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                </select>
                <!--Объект.Конец-->

                <!--Состояние-->
                <label for="<%=Components.stateObjectForFilterJournal%>"><b>Состояние</b></label>
                <select name="<%=Components.stateObjectForFilterJournal%>"
                        id="<%=Components.stateObjectForFilterJournal%>"
                        onchange="document.getElementById('filterForm').submit()">
                    <%
                        if (stateObjectForFilter == null) {
                    %>
                    <option selected value="">Все</option>
                    <%
                    } else {
                    %>
                    <option value="">Все</option>
                    <%
                        }
                    %>
                    <%
                        for (StateObject stateObject : stateObjectList) {
                            if (stateObjectForFilter != null && stateObjectForFilter.equals(stateObject)) {
                    %>
                    <option selected value="<%=stateObject.name()%>"><%=stateObject.toString()%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=stateObject.name()%>"><%=stateObject.toString()%>
                    </option>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                </select>
                <!--Пользователь.Конец-->
            </p>
        </div>

        <div style="float:left; width:15%;vertical-align: center;">
            <p align="center">
                <!--Пользователь-->
                <label for="<%=Components.userForFilterJournal%>"><b>Пользователь</b></label>
                <select name="<%=Components.userForFilterJournal%>" id="<%=Components.userForFilterJournal%>"
                        onchange="document.getElementById('filterForm').submit()">
                    <%
                        if (user == null) {
                    %>
                    <option selected value="-1">Все</option>
                    <%
                    } else {
                    %>
                    <option value="-1">Все</option>
                    <%
                        }
                    %>
                    <%
                        for (Users userFromList : usersList) {
                            if (user != null && user.getId().equals(userFromList.getId())) {
                    %>
                    <option selected value="<%=userFromList.getId()%>"><%=userFromList.toString()%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=userFromList.getId()%>"><%=userFromList.toString()%>
                    </option>
                    <%
                        }
                    %>
                    <%
                        }
                    %>
                </select>
                <!--Пользователь.Конец-->
            </p>
        </div>

    </div>
</form>

<div>
    <table>
        <tr>
            <th><h1>Дата</h1></th>
            <th><h1>Событие</h1></th>
            <th><h1>Состояние</h1></th>
            <th><h1>Объект</h1></th>
            <th><h1>Пользователь</h1></th>
        </tr>
        <%
            for (Journal journal : journalList) {
                String classTr = "no_delete";
                if (journal.getObject() != null) {
                    if (Employee.class.equals(journal.getObject().getClass())) {
                        Employee object = (Employee) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Workplace.class.equals(journal.getObject().getClass())) {
                        Workplace object = (Workplace) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Users.class.equals(journal.getObject().getClass())) {
                        Users object = (Users) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Accounting1C.class.equals(journal.getObject().getClass())) {
                        Accounting1C object = (Accounting1C) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Computer.class.equals(journal.getObject().getClass())) {
                        Computer object = (Computer) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Mfd.class.equals(journal.getObject().getClass())) {
                        Mfd object = (Mfd) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Monitor.class.equals(journal.getObject().getClass())) {
                        Monitor object = (Monitor) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Printer.class.equals(journal.getObject().getClass())) {
                        Printer object = (Printer) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Scanner.class.equals(journal.getObject().getClass())) {
                        Scanner object = (Scanner) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (Ups.class.equals(journal.getObject().getClass())) {
                        Ups object = (Ups) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    }
                }

        %>
        <tr class="<%=classTr%>">
            <td><%=journal.getTime()%>
            </td>
            <td>
                <h3><%=journal.getEvent()%>.</h3>
                <b>Тип объекта: </b><%=TypeObject.valueOf(journal.getTypeObject())%>
                <%
                    if (journal.getParameter() != null && !journal.getParameter().isEmpty()) {
                %>
                <b>Параметр: </b><%=TypeParameter.valueOf(journal.getParameter())%>.
                <%
                    }
                %>

            </td>
            <%
                TypeEvent typeEventForJournal = TypeEvent.valueOf(journal.getTypeEvent());
                if (TypeEvent.UPDATE.equals(typeEventForJournal) || TypeEvent.ACCOUNTING1C_MOVING.equals(typeEventForJournal)) {
            %>
            <td>
                <p>
                    Было:  <%=journal.getOldValue()%><br>
                    Стало: <%=journal.getNewValue()%>
                </p>
            </td>
            <%
            } else {
            %>
            <td/>
            <%
                }
            %>
            <td><%=journal.getObject() != null ? journal.getObject().toString() : journal.getObjectStr()%>
            </td>
            <td><%=journal.getUser() != null ? journal.getUser().toString() :
                    journal.getUserStr() != null ? journal.getUserStr() : ""%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
</body>
</html>
