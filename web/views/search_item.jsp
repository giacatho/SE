<%-- 
    Document   : search_item
    Created on : 03-Mar-2016, 07:43:08
    Author     : Quoc
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="search-item">
    <div class="input-group">
        <div class="input-group-btn">         
            <select class="btn btn-default select-operator" >
                <option value="0">AND</option>    
                <option value="1">OR</option>  
                <option value="2">NOT</option>  
            </select>
        </div>
        <input type="text" class="form-control">
        <div class="input-group-btn" style="padding-left: 10px;">                
            <select class="btn btn-default select-field" >
                <option value="-1">Select a Field (optional)</option>
                <c:choose>
                    <c:when test="${!empty requestScope.fields}">
                        <c:forEach var="entry" items="${requestScope.fields}">
                            <option value="${entry.value}"><c:out value="${entry.name}"></c:out></option>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </select>
            <button type="button" class="btn btn-default btn-remove"><i class="ion ion-minus"></i></button>
        </div>
    </div>
</div>