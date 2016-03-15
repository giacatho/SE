<%-- 
    Document   : search_result
    Created on : 03-Mar-2016, 21:45:57
    Author     : Quoc
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<c:choose>
    <c:when test="${result != null}">
        <h3>Found ${result.noHits} documents in ${result.searchTime} miliseconds. </h3>
        <c:forEach var="resultItem" items="${result.resultItems}">
			<div class="result-item">
				<div class="title">Title: ${resultItem.docTitle}</div>
				<div class="key">Key: ${resultItem.docKey}</div>
				<div class="score">Score: ${resultItem.searchScore}</div>
			</div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <h3>Fail to search!</h3>
    </c:otherwise>
</c:choose>