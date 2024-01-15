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
<title><c:out value="${pageTitle}" default="종목정보-리스트" /></title>
</head>
<style>
.table tbody tr.highlight td {
  background-color: #EAF0F7;
}
</style>
<body>
<!-- =================================================== -->
<jsp:include page="../../common/header.jsp" flush="false" />
<!-- =================================================== -->
<main class="container mx-3 my-3">

    <h2><i class="fa-solid fa-cube my-3"></i> 대표계정코드 관리</h2>
  
    <div class="container-lg p-3 border border-2 rounded-1">
        <input type="text" class="form-control w-50 d-inline align-middle" placeholder="검색어(대표계정코드/대표계정명)를 입력하세요" id="searchText" name="searchText" value="${param.searchText}">
        <a class="btn d-inline align-middle btn-primary btnRetrieve"><i class="fa-solid fa-search"></i> 조회</a>
        <a class="btn d-inline align-middle btn-secondary btnInit"><i class="fa-solid fa-backspace"></i> 초기화</a>
        <a class="btn d-inline align-middle btn-success" href="/jnl/repr_acnt/insert">  <i class="fa-solid fa-pencil-alt"></i> 등록</a>
    </div>

    <table class="table table-sm table-hover mt-3 itemTable" style="font-size:small">
      <thead class="table-light">
        <tr class="text-center">
          <th scope="col" style="width:20px">No</th>
          <th scope="col" style="width:100px">대표계정코드</th>
          <th scope="col">대표계정명</th>
          <th scope="col">속성</th>
          <th scope="col" style="width:85px">상대 대표계정코드</th>
          <th></th>
        </tr>
      </thead>
      <tbody class="table-group-divider" >
        <c:forEach var="jnl11" items="${list}" varStatus="status">
            <tr class="align-middle">
              <td scope="row" class="text-center fw-bold">${status.count + pageAttr.offset }</td>
              <td class="text-center">${jnl11.jnl11ReprAcntCd }</td>
              <td class="text-center">${jnl11.jnl11ReprAcntNm }</td>
              <td class="text-center">${jnl11.jnl11AcntAttributeNm }</td>
              <td class="text-center">${jnl11.jnl11TgtReprAcntCd }</td>
              <td class="text-end">
                  <button class="btn btn-primary btn-sm btnModify" data-jnl11-cd="${jnl11.jnl11ReprAcntCd }"><span><i class="fa-regular fa-pen-to-square"></i></span></button>
                  <button class="btn btn-danger btn-sm btnDelete" data-jnl11-cd="${jnl11.jnl11ReprAcntCd }" data-jnl11-nm="${jnl11.jnl11ReprAcntNm }"><span><i class="fa-regular fa-trash-can"></i></span></button>
              </td>
            </tr>
        </c:forEach>
      </tbody>
    </table>
	<form id="form1" action="/jnl/repr_acnt/list"  method="GET">
	<kfs:Pagination pageAttr="${pageAttr}" id="Pagination1" functionName="go"></kfs:Pagination>
	<input type="hidden" name="searchText" value="${param.searchText}">
	<input type="hidden" name="pageSize" value="${pageAttr.pageSize }"/>
	<input type="hidden" name="currentPageNumber" value="1"/>
	</form>
	<kfs:PageInfo pageAttr="${pageAttr}" id="PageInfo1"></kfs:PageInfo>
</main>
<!-- =================================================== -->
<jsp:include page="../../common/footer.jsp" flush="false" />
<!-- -================================================== -->
<script>
$(document).ready(function () {
	console.log('ready...');
	
	//화면뜨면 검색창에 포커스 
	$("#searchText").focus();
	
	//검색창에서 enter이면 조회
	$("#searchText").on("keyup",function(key){
        if(key.keyCode==13) { //enter
        	$('.btnRetrieve').trigger('click');
        }else if(key.keyCode == 46){ //DEL
        	$(this).val('');
        }
	});
	
	// 조회 버튼 클릭 : searchText를 가지고 selectList
   $('.btnRetrieve').on('click', function(){
        var searchText = $('#searchText').val();
        AssetUtil.submitGet('/jnl/repr_acnt/list', {searchText: searchText});
    });
	// 초기화 버튼
    $('.btnInit').on('click', function(){
    	 AssetUtil.submitGet('/jnl/repr_acnt/list', {searchText: null});
    });
	
    // 수정버튼
    $(".btnModify").on("click", function(){
		var jnl11ReprAcntCd = $(this).data('jnl11-cd');
		AssetUtil.submitGet('/jnl/repr_acnt/update', {'jnl11ReprAcntCd' : jnl11ReprAcntCd});
	});
    
    // 삭제 버튼
    $('.btnDelete').on("click", function(){
    	var jnl11ReprAcntCd = $(this).data('jnl11-cd');
    	var jnl11ReprAcntNm = $(this).data('jnl11-nm');
    	
    	if(confirm(jnl11ReprAcntNm + " 대표계정코드를 삭제하시겠습니까?")){
    		AssetUtil.submitGet('/jnl/repr_acnt/delete', {'jnl11ReprAcntCd' : jnl11ReprAcntCd});
        }
    });
});

function go(no){
	$('#form1 input[name=currentPageNumber]').val(no);
	$('#form1').submit();
	}
</script>
</body>
</html>