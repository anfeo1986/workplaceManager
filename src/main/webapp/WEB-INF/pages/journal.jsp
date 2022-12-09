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

%>

<div align="center">
    <h1>Фильтры</h1>
    <form action="<%=Pages.journal%>" method="get" id="filterForm">
        <p>
            От: <input type="date" name="calendarFrom">
            До: <input type="date" name="calendarBefore">

            <label for="<%=Components.typeObjectForJournal%>">Тип объекта</label>
            <select name="<%=Components.typeObjectForJournal%>" id="<%=Components.typeObjectForJournal%>"
                    onchange="document.getElementById('filterForm').submit()">
                <%
                    if (typeObject == null) {
                %>
                <option selected value=""/>
                <%
                } else {
                %>
                <option value=""/>
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

            <label for="<%=Components.objectForJournal%>">Объект</label>
            <select name="<%=Components.objectForJournal%>" id="<%=Components.objectForJournal%>"
                    onchange="document.getElementById('filterForm').submit()">
                <%
                    if (objectId == null || objectId <= 0) {
                %>
                <option selected value="-1"/>
                <%
                } else {
                %>
                <option value="-1"/>
                <%
                    }
                %>
                <%
                    HashSet<Long> objectIdSet = new HashSet<>();

                    for (Journal journal : journalList) {
                        if (journal.getIdObject() == null || journal.getIdObject() <= 0) {
                            continue;
                        }
                        if (!objectIdSet.contains(journal.getIdObject())) {
                            objectIdSet.add(journal.getIdObject());
                        } else {
                            continue;
                        }
                        if (objectId == journal.getIdObject()) {
                %>
                <option selected value="<%=journal.getIdObject()%>">
                    <%=journal.getObject() != null ? journal.getObject().toString() :
                            journal.getObjectStr() != null ? journal.getObjectStr() : ""%>
                </option>
                <%
                } else {
                %>
                <option value="<%=journal.getIdObject()%>">
                    <%=journal.getObject() != null ? journal.getObject().toString() :
                            journal.getObjectStr() != null ? journal.getObjectStr() : ""%>
                </option>
                <%
                    }
                %>
                <%
                    }
                %>
            </select>
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
                if (journal.getTypeObject() != null && !journal.getTypeObject().isEmpty() &&
                        journal.getObject() != null && typeObject != null) {
                    if (TypeObject.EMPLOYEE.equals(typeObject)) {
                        Employee object = (Employee) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.WORKPLACE.equals(typeObject)) {
                        Workplace object = (Workplace) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.USER.equals(typeObject)) {
                        Users object = (Users) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.ACCOUNTING1C.equals(typeObject)) {
                        Accounting1C object = (Accounting1C) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.COMPUTER.equals(typeObject)) {
                        Computer object = (Computer) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.MFD.equals(typeObject)) {
                        Mfd object = (Mfd) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.MONITOR.equals(typeObject)) {
                        Monitor object = (Monitor) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.PRINTER.equals(typeObject)) {
                        Printer object = (Printer) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.SCANNER.equals(typeObject)) {
                        Scanner object = (Scanner) journal.getObject();
                        if (object.getDeleted() != null && object.getDeleted()) {
                            classTr = "delete";
                        }
                    } else if (TypeObject.UPS.equals(typeObject)) {
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
                TypeEvent typeEvent = TypeEvent.valueOf(journal.getTypeEvent());
                if (TypeEvent.UPDATE.equals(typeEvent) || TypeEvent.ACCOUNTING1C_MOVING.equals(typeEvent)) {
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
