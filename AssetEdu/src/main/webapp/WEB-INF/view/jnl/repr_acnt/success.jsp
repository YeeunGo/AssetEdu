<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="kfs" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="asset"  uri="/WEB-INF/asset-tags/asset.tld"%>
    
<!DOCTYPE html>
<html>
<head>
<!-- =================================================== -->
<jsp:include page="../../common/meta_css.jsp" flush="false" />
<!-- =================================================== -->
<title><c:out value="${pageTitle}" default="Acnt Add Success" /></title>
</head>
<body>
<!-- =================================================== -->
<jsp:include page="../../common/header.jsp" flush="false" />
<!-- =================================================== -->
<c:set var="baseUrl" value="/jnl/acnt"/>
<main class="container mx-3 my-3">

	<h2><i class="fa-solid fa-cube my-3"></i> 대표계정코드 관리 > 대표계정코드 등록 Success</h2>
	
	<div class="border-top border-2 p-4">
		<div class="text-left py-5">
			<h1>${msg}</h1>
		</div>
		<div>
			<table>
			<tr>
			<td>
				<a href="<c:url value='/jnl/repr_acnt/list '/>" class="btn btn-primary">리스트</a>
			</td>
			<td>
				<c:if test="${mode eq 'insert'}">
					<a href="/jnl/repr_acnt/insert" class="btn btn-warning">계속입력</a>
				</c:if>
				<c:if test="${mode eq 'update'}">
					<form id="form1" method="GET" action="/jnl/repr_acnt/update">
						<input type="hidden" name="jnl11ReprAcntCd" value="${jnl11ReprAcntCd}"/>
						<button type="submit" class="btn btn-warning">계속수정</button>
					</form>
				</c:if>
			</td>
			</tr>
			</table>
		</div>
	</div>
</main>
<!-- =================================================== -->
<jsp:include page="../../common/footer.jsp" flush="false" />
<!-- -================================================== -->
<script>
$(document).ready(function () {
	console.log('ready...');
});
</script>	
</body>
</html>