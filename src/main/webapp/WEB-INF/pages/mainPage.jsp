<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" type="text/css"/>

    <title>JSP Templates</title>

</head>
<!--<body background='graphics/background.jpg'>-->
<body>
<header>
    <div align="center">
        <%@include file='header.jsp' %>
    </div>
</header>
<input type="hidden" name="token" value="${token}">
<div>
    <table>
        <%--  <tr valign='top' align='center'>
              <td>
                  <%@include file='header.jsp' %>
              </td>
          </tr>
      --%>
        <tr valign='top' align='center'>
            <c:if test="${page == 'computer'}">
                <td>
                    <%@include file="computerTable.jsp" %>
                </td>
            </c:if>
            <c:if test="${page == 'employee'}">
                <td>
                    <%@include file='employeeTable.jsp' %>
                </td>
            </c:if>
            <c:if test="${page == 'workplace'}">
                <td>
                    <%@include file='workplaceTable.jsp' %>
                </td>
            </c:if>
            <c:if test="${page == 'accounting1c'}">
                <td>
                    <%@include file="accounting1cTable.jsp" %>
                </td>
            </c:if>
            <c:if test="${page == 'monitor'}">
                <td>
                    <%@include file="equipmentTable.jsp" %>
                </td>
            </c:if>
            <c:if test="${page == 'printer'}">
                <td>
                    <%@include file="equipmentTable.jsp" %>
                </td>
            </c:if>
            <c:if test="${page == 'scanner'}">
                <td>
                    <%@include file="equipmentTable.jsp" %>
                </td>
            </c:if>
            <c:if test="${page == 'mfd'}">
                <td>
                    <%@include file="equipmentTable.jsp" %>
                </td>
            </c:if>
            <c:if test="${page == 'ups'}">
                <td>
                    <%@include file="equipmentTable.jsp" %>
                </td>
            </c:if>
        </tr>
    </table>
</div>

</body>
</html>