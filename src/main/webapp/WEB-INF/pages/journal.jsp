<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.*" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="workplaceManager.db.domain.*" %>
<%@ page import="workplaceManager.db.domain.Scanner" %><%--
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
            document.getElementById('<%=Components.objectForJournal%>').selectedIndex = 0;
            document.getElementById('<%=Components.eventForJournal%>').selectedIndex = 0;

            document.getElementById('filterForm').submit();
        }
    </script>
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
    SortedMap<String, Long> objectIdList = (SortedMap<String, Long>) request.getAttribute(Parameters.journalObjectIdList);

    String typeEventStr = (String) request.getAttribute(Parameters.journalFilterTypeEvent);
    TypeEvent typeEvent = null;
    if(typeEventStr != null && !typeEventStr.isEmpty()) {
        typeEvent = TypeEvent.valueOf(typeEventStr);
    }
    List<TypeEvent> typeEventList = (List<TypeEvent>) request.getAttribute(Parameters.journalTypeEventList);

%>

<div align="center">
    <h1>Фильтры</h1>
    <form action="<%=Pages.journal%>" method="get" id="filterForm">
        <p>
            От: <input type="date" name="calendarFrom">
            До: <input type="date" name="calendarBefore">

            <!--Тип объекта-->
            <label for="<%=Components.typeObjectForJournal%>">Тип объекта</label>
            <select name="<%=Components.typeObjectForJournal%>" id="<%=Components.typeObjectForJournal%>"
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

            <!--Объект-->
            <label for="<%=Components.objectForJournal%>">Объект</label>
            <select name="<%=Components.objectForJournal%>" id="<%=Components.objectForJournal%>"
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

            <!--Тип события-->
            <label for="<%=Components.eventForJournal%>">Событие</label>
            <select name="<%=Components.eventForJournal%>" id="<%=Components.eventForJournal%>"
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
        </p>
    </form>
</div>

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
                <b>Параметр: </b><%=journal.getParameter()%>.
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
