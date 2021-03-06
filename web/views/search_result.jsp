<%-- 
    Document   : search_result
    Created on : 03-Mar-2016, 21:45:57
    Author     : Quoc
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<c:choose>
    <c:when test="${result != null}">
        <h5>About ${result.noHits} results (${result.searchTime} miliseconds)</h3>
        <c:forEach var="resultItem" items="${result.resultItems}">
			<div class="result-item">
				<div class="title">${resultItem.docTitleBeauty}</div>
				<c:if test="${resultItem.docAuthors.size() gt 0}">
				<div class="author">${fn:length(resultItem.docAuthors)} Author(s): ${resultItem.docAuthorsBeauty}</div>
				</c:if>
				<div class="key">DocID: ${resultItem.docKey}</div>
				<div class="pubyear">Public Year: ${resultItem.docPubYear}</div>
				<div class="pubvenue">Public Venue: ${resultItem.docPubVenue}</div>
				<div class="score">Score: ${resultItem.searchScore}</div>
			</div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <h3>Fail to search!</h3>
    </c:otherwise>
</c:choose>