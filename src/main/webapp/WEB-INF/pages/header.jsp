<%@ page import="workplaceManager.controller.MainController" %>
<%@ page import="workplaceManager.Parameters" %>
<%@ page import="workplaceManager.Pages" %>
<%@ page import="workplaceManager.db.domain.TypeEquipment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String typePageStr = (String) request.getAttribute(Parameters.page);
    MainController.TypePage typePage = null;
    if (typePageStr != null && !typePageStr.isEmpty()) {
        typePage = MainController.TypePage.valueOf(typePageStr);
    }
    String buttonStyle = "button";
    String buttonStyleSelect = "button_select";
    String userName = (String) request.getSession().getAttribute(Parameters.login);
    Role roleHeader = request.getSession().getAttribute(Parameters.role) != null ?
            (Role) request.getSession().getAttribute(Parameters.role) : Role.USER;

    Long idObject = request.getParameter(Parameters.id) != null ?
            Long.parseLong(request.getParameter(Parameters.id)) : null;
%>


<div class="navbar_left">
    <a href="<%=baseUrl + Pages.workplace%>">Главная</a>
    <div class="dropdown">
        <button class="dropbtn"> Страницы
            <i class="fa fa-caret-down"></i>
        </button>
        <div class="dropdown-content">
            <a href="<%=baseUrl + Pages.workplace%>">Рабочие места</a>
            <a href="<%=baseUrl + Pages.computer%>">Компьютеры</a>
            <a href="<%=baseUrl + Pages.monitor%>">Мониторы</a>
            <a href="<%=baseUrl + Pages.mfd%>">МФУ</a>
            <a href="<%=baseUrl + Pages.printer%>">Принтеры</a>
            <a href="<%=baseUrl + Pages.scanner%>">Сканеры</a>
            <a href="<%=baseUrl + Pages.ups%>">ИБП</a>
            <a href="<%=baseUrl + Pages.virtualMachine%>">Виртуальные машины</a>
            <a href="<%=baseUrl + Pages.employee%>">Сотрудники</a>
            <a href="<%=baseUrl + Pages.accounting1c%>">Бухгалтерия</a>
        </div>
    </div>
    <%
        if (Role.ADMIN.equals(roleHeader) && typePage != null && idObject == null) {
    %>
    <div class="dropdown">
        <button class="dropbtn"> Действия
            <i class="fa fa-caret-down"></i>
        </button>
        <div class="dropdown-content">
            <%
                if (MainController.TypePage.workplace.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateWorkplace%>?<%=Parameters.redirect%>=<%=Pages.workplace%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить рабочее место
            </a>
            <%
            } else if (MainController.TypePage.computer.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.computer%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.COMPUTER%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить компьютер
            </a>
            <%
            } else if (MainController.TypePage.monitor.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.monitor%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MONITOR%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить монитор
            </a>
            <%
            } else if (MainController.TypePage.mfd.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.mfd%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.MFD%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить МФУ
            </a>
            <%
            } else if (MainController.TypePage.printer.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.printer%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.PRINTER%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить принтер
            </a>
            <%
            } else if (MainController.TypePage.scanner.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.scanner%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.SCANNER%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить сканер
            </a>
            <%
            } else if (MainController.TypePage.ups.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEquipment%>?<%=Parameters.redirect%>=<%=Pages.ups%>&<%=Parameters.typeEquipment%>=<%=TypeEquipment.UPS%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить ИБП
            </a>
            <%
            } else if (MainController.TypePage.employee.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateEmployee%>?<%=Parameters.redirect%>=<%=Pages.employee%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить сотрудника
            </a>
            <%
            } else if (MainController.TypePage.accounting1c.equals(typePage)) {
            %>
            <a href="<%=baseUrl + Pages.addUpdateAccounting1C%>?<%=Parameters.redirect%>=<%=Pages.accounting1c%>&<%=Parameters.page%>=<%=request.getAttribute(Parameters.page)%>"
               target="_blank">
                Добавить запись в бухгалтерии
            </a>
            <%
                }
            %>
        </div>
    </div>
    <%
        }
    %>
    <%
        if (MainController.TypePage.workplace.equals(typePage)) {
    %>
    <a href="<%=baseUrl + Pages.mainSettings%>" target="_blank">Настройки</a>
    <%
        }
    %>
    <a href="<%=baseUrl + Pages.journal%>" target="_blank">Журнал</a>

    <div class="navbar_right">
        <span><%=userName%></span>
        <a href="<%=baseUrl + Pages.logout%>">Выход</a>
    </div>
</div>
<br>
