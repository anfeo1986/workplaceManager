<%@ page import="workplaceManager.db.domain.Computer" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="workplaceManager.db.domain.components.Ram" %>
<%@ page import="workplaceManager.db.domain.components.HardDrive" %>
<%@ page import="workplaceManager.db.domain.components.Processor" %>
<%@ page import="workplaceManager.db.domain.components.VideoCard" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div align="center"><h1>${title}</h1></div>

<%
    String typeEquipment = (String) request.getAttribute(Parameters.typeEquipment);
    String redirect = (String) request.getAttribute(Parameters.page);
    String title = (String) request.getAttribute(Parameters.title);
    List<Computer> computerList = (List<Computer>) request.getAttribute(Parameters.equipmentList);

%>

<table>
    <tr>
        <th>id</th>
        <th><%=title%></th>
        <th>ОС</th>
        <th>Процессор</th>
        <th>Материнская плата</th>
        <th>Оперативная память</th>
        <th>Жесткие диски</th>
        <th>Видеокарта</th>
        <th>Рабочее место</th>
        <th>Бухгалтерия</th>
        <%
            if(Role.ADMIN.equals(role)) {
        %>
        <th/>
        <%
            }
        %>
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
        <td><a href="<%=baseUrl + Pages.addUpdateEquipment%>?id=<%=computer.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
            <%=computer%>
        </a>
        </td>
        <td><%=computer.getOperationSystem() != null ? computer.getOperationSystem() : ""%>
        </td>
        <%
            if (!CollectionUtils.isEmpty(computer.getProcessorList())) {
        %>
        <td>
            <%
                for (Processor processor : computer.getProcessorList()) {
            %>
            <p><%=processor%>
            </p>
            <%
                }
            %>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <td><%=computer.getMotherBoard() != null ? computer.getMotherBoard() : ""%>
        </td>
        <%
            if (!CollectionUtils.isEmpty(computer.getRamList())) {
        %>
        <td>
            <%
                for (Ram ram : computer.getRamList()) {
            %>
            <p><%=ram%>
            </p>
            <%
                }
            %>
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
            <%
                for (HardDrive hardDrive : computer.getHardDriveList()) {
            %>
            <p><%=hardDrive%>
            </p>
            <%
                }
            %>
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
            <%
                for (VideoCard videoCard : computer.getVideoCardList()) {
            %>
            <p><%=videoCard%>
            </p>
            <%
                }
            %>
        </td>
        <%
        } else {
        %>
        <td/>
        <%
            }
        %>
        <%
            if (computer.getWorkplace() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateWorkplace%>?<%=Parameters.id%>=<%=computer.getWorkplace().getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                <%=ReplaceString.replace(computer.getWorkplace().getTitle())%>
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
            if (computer.getAccounting1C() != null) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.id%>=<%=computer.getAccounting1C().getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                <%=computer.getAccounting1C()%>
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
            if (Role.ADMIN.equals(role)) {
        %>
        <td>
            <a href="<%=baseUrl + Pages.deleteEquipmentPost%>?<%=Parameters.id%>=<%=computer.getId()%>&<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=redirect%>&<%=Parameters.typeEquipment%>=<%=typeEquipment%>">
                Удалить
            </a>
        </td>
        <%
                }
            }
        %>
    </tr>
</table>