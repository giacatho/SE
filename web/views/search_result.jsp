<%-- 
    Document   : search_result
    Created on : 03-Mar-2016, 21:45:57
    Author     : Quoc
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
    <c:when test="${result != null}">
        <div id="ResultCategoryDisplay" class="col col-sm-3" style="display: table-cell; color: dodgerblue;">
            <h5 style="color:black">Load time: ${result.categoryTime} miliseconds</h5>
            
            <a href="#">All(${fn:length(result.resultItems)})<i class="ion ion-checkmark" style=" margin-left: 7px"/></a><br/>
            <c:forEach var="category" items="${result.categories}">
                <a href="#" style="margin-left: 15px" data-docs="<c:forEach var="docId" items="${category.docIds}"><c:out value="${docId}"/>,</c:forEach>">${category.name} (${category.size})<i class="ion ion-checkmark" style="margin-left: 7px; display: none"/></a><br/>
            </c:forEach>            
        </div>
        <div style="display: table-cell; width: 100%">    
            <h5>About ${result.noHits} results (${result.searchTime} miliseconds)</h5>
            <c:forEach var="resultItem" items="${result.resultItems}">
                <div class="result-item">
                    <input type="hidden" value="${resultItem.id}"/>
                    <div class="title">${resultItem.docTitleBeauty}</div>
                    <c:if test="${resultItem.docAuthors.size() gt 0}">
                    <div class="author">${fn:length(resultItem.docAuthors)} Author(s): ${resultItem.docAuthorsBeauty}</div>
                    </c:if>
                    <div class="key">DocID: ${resultItem.docKey}</div>
                    <div class="pubyear">Publication Year: ${resultItem.docPubYearBeauty}</div>
                    <div class="pubvenue">Publication Venue: ${resultItem.docPubVenueBeauty}</div>
                    <div class="score">Score: ${resultItem.searchScore}</div>
                </div>
            </c:forEach>        
        </div>
    </c:when>
    <c:otherwise>
        <h3>Fail to search!</h3>
    </c:otherwise>
</c:choose>

<script type="text/javascript">
    $(document).ready(function(){        
        $('#ResultCategoryDisplay').find('a').each(function(){            
            if ($(this).attr("data-docs")) {
                var data = $(this).attr("data-docs") //.toString();
                $(this).attr("data-docs", data.substr(0, data.length-1));
            }
            
            $(this).on('click', function() {              
                $(this).parent().find('i').each(function() {
                    $(this).hide();
                });
                $(this).children('i').show();
                
                if ($(this).attr('data-docs')) {                    
                    var docIds = $(this).attr('data-docs').split(',');
                    
                    $('.result-item').each(function() {                        
                        var id = $(this).closestChild('input').val();
                        if (docIds.indexOf(id) > -1) $(this).show();
                        else $(this).hide();
                    });
                }                
                else {
                    $('.result-item').show();
                }
            });
        });
    });
</script>