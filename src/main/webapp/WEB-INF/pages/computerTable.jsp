<%@ page import="workplaceManager.db.domain.Computer" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="workplaceManager.db.domain.components.Ram" %>
<%@ page import="workplaceManager.db.domain.components.HardDrive" %>
<%@ page import="workplaceManager.db.domain.components.Processor" %>
<%@ page import="workplaceManager.db.domain.components.VideoCard" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String typeEquipment = (String) request.getAttribute(Parameters.typeEquipment);
    String redirect = (String) request.getAttribute(Parameters.page);
    String title = (String) request.getAttribute(Parameters.title);
    List<Computer> computerList = request.getAttribute(Parameters.equipmentList) != null ?
            (List<Computer>) request.getAttribute(Parameters.equipmentList) : new ArrayList<>();
    Role roleInComputer = request.getSession().getAttribute(Parameters.role) != null ?
            (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;
    Long idComputer = request.getParameter(Parameters.id) != null ?
            Long.parseLong(request.getParameter(Parameters.id)) : null;
%>

<div align="center"><h1>${title}</h1></div>
<%
    if (idComputer == null) {
%>
<%
    }
%>


<table>
    <tr>
        <th>Номер</th>
        <th><%=title%>
        </th>
        <th>Рабочее место</th>
        <th>ОС</th>
        <th>Материнская плата</th>
        <th>Процессор</th>
        <th>Оперативная память</th>
        <th>Жесткие диски</th>
        <th>Видеокарта</th>
        <th>Комментарий</th>
        <th>Бухгалтерия</th>

    </tr>
    <%
        int count = 1;
        for (Computer computer : computerList) {
    %>
    <tr>
        <td><%=count%>
        </td>
        <%
            count++;
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.id%>=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=computer.toStringHtmlSelectIp()%>
            </a>
        </td>
        <%
            if (computer.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.workplace%>?<%=Parameters.id%>=<%=computer.getWorkplace().getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=computer.getWorkplace().getTitleHtml()%>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=computer.getOperationSystem() != null ? computer.getOperationSystem().toStringHtml() : ""%>
            </a>
        </td>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=computer.getMotherBoard() != null ? computer.getMotherBoard() : ""%>
            </a>
        </td>
        <%
            if (!CollectionUtils.isEmpty(computer.getProcessorList())) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%
                    for (Processor processor : computer.getProcessorList()) {
                %>
                <p><%=processor.toStringHtml()%>
                </p>
                <%
                    }
                %>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>

        <%
            if (!CollectionUtils.isEmpty(computer.getRamList())) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%
                    for (Ram ram : computer.getRamList()) {
                %>
                <p><%=ram.toStringHtml()%>
                </p>
                <%
                    }
                %>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <%
            if (!CollectionUtils.isEmpty(computer.getHardDriveList())) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%
                    for (HardDrive hardDrive : computer.getHardDriveList()) {
                %>
                <p><%=hardDrive.toStringHtml()%>
                </p>
                <%
                    }
                %>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <%
            if (!CollectionUtils.isEmpty(computer.getVideoCardList())) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%
                    for (VideoCard videoCard : computer.getVideoCardList()) {
                %>
                <p><%=videoCard%>
                </p>
                <%
                    }
                %>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=computer.getCommentHtml()%>
            </a>
        </td>
        <%
            if (computer.getAccounting1C() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.accounting1c%>?<%=Parameters.id%>=<%=computer.getAccounting1C().getId()%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>"
               target="_blank">
                <%=computer.getAccounting1C().toStringHtml()%>
            </a>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>

        <%
            }
        %>
    </tr>
</table>

<%
    if (idComputer != null && idComputer > 0) {
%>
<div align="center">
    <p>
        <a onclick="close_window(); return false;" class="button">Назад</a>
    </p>
</div>
<%
    }
%>
