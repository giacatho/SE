<%-- 
    Document   : search_result
    Created on : 03-Mar-2016, 21:45:57
    Author     : Quoc
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
    <c:when test="${result != null}">
        Found ${result.hits} documents in <c:out value="${result.searchTime}"></c:out> miliseconds. <br/>
        <c:forEach var="entry" items="${result.outputs}">
            <div>${entry.html}</div> 
        </c:forEach>
    </c:when>
    <c:otherwise>
        Fail to search!
    </c:otherwise>
</c:choose>