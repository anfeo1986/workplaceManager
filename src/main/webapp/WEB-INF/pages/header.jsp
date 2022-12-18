<%@ page import="workplaceManager.controller.MainController" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.db.domain.TypeEquipment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String typePageStr = (String) request.getAttribute(Parameters.page);
    MainController.TypePage typePage = null;
    if(typePageStr != null && !typePageStr.isEmpty()) {
        typePage = MainController.TypePage.valueOf(typePageStr);
    }
    String buttonStyle = "button";
    String buttonStyleSelect = "button_select";
    String userName = (String) request.getAttribute(Parameters.userName);
    Role roleHeader = (Role) request.getAttribute(Parameters.role);
%>


<div class="navbar_left">
    <a href="<%=baseUrl + Pages.workplace%>?<%=Parameters.token%>=<%=token%>">Главная</a>
    <div class="dropdown">
        <button class="dropbtn"> Страницы
            <i class="fa fa-caret-down"></i>
        </button>
        <div class="dropdown-content">
            <a href="<%=baseUrl + Pages.workplace%>?<%=Parameters.token%>=<%=token%>">Рабочие места</a>
            <a href="<%=baseUrl + Pages.computer%>?<%=Parameters.token%>=<%=token%>">Компьютеры</a>
            <a href="<%=baseUrl + Pages.monitor%>?<%=Parameters.token%>=<%=token%>">Мониторы</a>
            <a href="<%=baseUrl + Pages.mfd%>?<%=Parameters.token%>=<%=token%>">МФУ</a>
            <a href="<%=baseUrl + Pages.printer%>?<%=Parameters.token%>=<%=token%>">Принтеры</a>
            <a href="<%=baseUrl + Pages.scanner%>?<%=Parameters.token%>=<%=token%>">Сканеры</a>
            <a href="<%=baseUrl + Pages.ups%>?<%=Parameters.token%>=<%=token%>">ИБП</a>
            <a href="<%=baseUrl + Pages.employee%>?<%=Parameters.token%>=<%=token%>">Сотрудники</a>
            <a href="<%=baseUrl + Pages.accounting1c%>?<%=Parameters.token%>=<%=token%>">Бухгалтерия</a>
        </div>
    </div>
    <%
        if (Role.ADMIN.equals(roleHeader) && typePage != null) {
    %>
    <div class="dropdown">
        <button class="dropbtn"> Действия
            <i class="fa fa-caret-down"></i>
        </button>
        <div class="dropdown-content">
            <%
                if (MainController.TypePage.workplace.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateWorkplace%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить рабочее место
            </a>
            <%
            } else if (MainController.TypePage.computer.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.computer%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить компьютер
            </a>
            <%
            } else if (MainController.TypePage.monitor.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.monitor%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить монитор
            </a>
            <%
            } else if (MainController.TypePage.mfd.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.mfd%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить МФУ
            </a>
            <%
            } else if (MainController.TypePage.printer.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.printer%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить принтер
            </a>
            <%
            } else if (MainController.TypePage.scanner.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.scanner%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить сканер
            </a>
            <%
            } else if (MainController.TypePage.ups.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.ups%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить ИБП
            </a>
            <%
            } else if (MainController.TypePage.employee.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEmployee%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.employee%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить сотрудника
            </a>
            <%
            } else if (MainController.TypePage.accounting1c.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.token%>=<%=token%>&<%=Parameters.redirect%>=<%=Pages.accounting1c%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>">
                Добавить сотрудника
            </a>
            <%
                }
            %>
        </div>
    </div>
    <%
        }
    %>
    <a href="<%=baseUrl + Pages.journal%>" target="_blank">Журнал</a>

    <div class="navbar_right">
        <span><%=userName%></span>
        <a href="<%=baseUrl%>?<%=Parameters.token%>=">Выход</a>
    </div>
</div>
<br>
