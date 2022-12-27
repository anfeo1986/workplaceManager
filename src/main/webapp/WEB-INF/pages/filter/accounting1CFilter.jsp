<%@ page import="workplaceManager.sorting.SortingAccounting1C" %>
<%@ page import="workplaceManager.sorting.FilterAccounting1C" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    SortingAccounting1C sortingFromRequest = SortingAccounting1C.INVENTORY_NUMBER;
    String sortingStr = request.getParameter(Parameters.accountingSorting);
    if (sortingStr != null) {
        sortingFromRequest = SortingAccounting1C.valueOf(sortingStr);
    }
    String baseUrlForFilter = (String) request.getAttribute(Parameters.baseUrl);

    FilterAccounting1C filter = request.getParameter(Parameters.accounting1CFilter) != null ?
            FilterAccounting1C.valueOf(request.getParameter(Parameters.accounting1CFilter)) : FilterAccounting1C.ALL;

    String findText = request.getParameter(Parameters.accounting1CFindText);
%>

<div class="wrapper" align="center">
    <form action="<%=baseUrlForFilter + Pages.accounting1c%>" method="get">
        <p>
            <b class="b_margin_left">Сортировка:</b>
            <select name="<%=Parameters.accountingSorting%>" id="<%=Parameters.accountingSorting%>">
                <%
                    for (SortingAccounting1C sortingAccounting1C : SortingAccounting1C.values()) {
                        if (sortingAccounting1C.equals(sortingFromRequest)) {
                %>
                <option selected value="<%=sortingAccounting1C.name()%>"><%=sortingAccounting1C.getTitle()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=sortingAccounting1C.name()%>"><%=sortingAccounting1C.getTitle()%>
                </option>
                <%
                        }
                    }
                %>
            </select>
            <b class="b_margin_left">Поиск:</b>
            <input type="text" name="<%=Parameters.accounting1CFindText%>" id="<%=Parameters.accounting1CFindText%>"
                   size="30"
                   value="<%=findText != null ? findText : ""%>">
            <b class="b_margin_left">Оборудование</b>
            <select name="<%=Parameters.accounting1CFilter%>" id="<%=Parameters.accounting1CFilter%>">
                <%
                    for (FilterAccounting1C filterAccounting1C : FilterAccounting1C.values()) {
                        if (filter.equals(filterAccounting1C)) {
                %>
                <option selected value="<%=filterAccounting1C.name()%>"><%=filterAccounting1C.getTitle()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=filterAccounting1C.name()%>"><%=filterAccounting1C.getTitle()%></option>
                <%
                        }
                    }
                %>
            </select>
            <input type="submit" value="Применить">
        </p>
    </form>
</div>