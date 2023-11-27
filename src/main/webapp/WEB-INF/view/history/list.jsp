<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/view/layout/header.jsp" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="col-sm-8">
      <h2>나의 내역 목록</h2>
      <h5>어서오세요</h5>
	  <div class="bg-light p-md-5 h-75">
	  <c:choose>
	  <c:when test = "${historyList != null}">
	  <table class="table">
			<thead>
				<tr>
					<th>내역 번호</th>
					<th>출금 금액</th>
					<th>총 금액</th>
					<th>보낸 날짜</th>
					<th>입금 계좌</th>
					<th>보낸 사람</th>
					<th>출금 계좌</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="account" items="${accountList}"> 
				<tr>
					<td>1111</td>
					<td>1111</td>
					<td>1111</td>
					<td>1111</td>
					<td>1111</td>
					<td>1111</td>
					<td>1111</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	  </c:when>
	  <c:otherwise>
	  	<p>출금내역이 없습니다.
	  </c:otherwise>
	  </c:choose>
	  
		
	  </div>		
    
    
    <%@ include file="/WEB-INF/view/layout/footer.jsp" %>
   