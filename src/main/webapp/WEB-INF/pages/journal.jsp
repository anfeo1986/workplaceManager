<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="workplaceManager.db.domain.Journal" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page import="workplaceManager.TypeEvent" %>
<%@ page import="workplaceManager.TypeObject" %><%--
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

    <title>JSP Templates</title>
</head>
<body>

<%
    List<Journal> journalList = new ArrayList<>();
    journalList = (List<Journal>) request.getAttribute(Parameters.journalList);
%>
<div>
    <table>
        <tr>
            <th><h1>Дата</h1></th>
            <th><h1>Событие</h1></th>
            <th><h1>Состояние</h1></th>
            <th><h1>Объект</h1></th>
        </tr>
        <%
            for (Journal journal : journalList) {
        %>
        <tr>
            <td><%=journal.getTime()%>
            </td>
            <td>
                <b><%=journal.getEvent()%>.</b><br>
                <b>Тип объекта: </b><%=TypeObject.valueOf(journal.getTypeObject())%>
                <b>Объект: </b><%=journal.getObjectStr()%>.
            </td>
            <%
                TypeEvent typeEvent = TypeEvent.valueOf(journal.getTypeEvent());
                if (TypeEvent.UPDATE.equals(typeEvent)) {
            %>
            <td>
                <p>Было:  <%=journal.getOldValue()%>
                </p>
                <p>Стало: <%=journal.getNewValue()%>
                </p>
            </td>
            <%
            } else {
            %>
            <td/>
            <%
                }
            %>
            <td><%=journal.getObject() == null ? "" : journal.getObject().toString()%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
</body>
</html>
