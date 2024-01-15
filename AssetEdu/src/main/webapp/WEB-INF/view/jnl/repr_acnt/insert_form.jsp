<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"     uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"   uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"  uri="http://www.springframework.org/tags/form"  %>
<%@ taglib prefix="asset" uri="/WEB-INF/asset-tags/asset.tld"%>
<%@ taglib prefix="kfs"   tagdir="/WEB-INF/tags"%>
    
<!DOCTYPE html>
<html>
<head>

<!-- =================================================== -->
<jsp:include page="../../common/meta_css.jsp" flush="false" />
<!-- =================================================== -->
<title><c:out value="${pageTitle}" default="주식종목정보-등록" /></title>
</head>
<body>
<!-- =================================================== -->
<jsp:include page="../../common/header.jsp" flush="false" />
<!-- =================================================== -->
<main class="container mx-3 my-3">

	<h2><i class="fa-solid fa-cube my-3"></i> 대표계정코드 관리 > 대표계정코드 등록</h2>
    <div class="border-top border-2 p-4">

        <form:form action="/jnl/repr_acnt/insert" method="POST" modelAttribute="jnl11ReprAcnt" class="validcheck" >
            <table class="table table-sm table-borderless">
                <tr class="align-middle">
                    <td class="text-end" style="width:150px">대표계정코드</td>
                    <td style="width:250px"><form:input type="text" class="form-control" path="jnl11ReprAcntCd" data-v-max-length="4" data-v-min-length="4" required="true" placeholder="계정코드 필수입력(4자리)"/></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">대표계정명</td>
                    <td colspan=3><form:input type="text" class="form-control" path="jnl11ReprAcntNm" required="true" placeholder="필수입력"/></td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">계정속성</td>
                    <td class="text-start" colspan=2>
                        <form:select path="jnl11AcntAttributeCd" class="form-select w-25"  required="true">
                            <form:option value="" />
                            <form:options items="${acntAttrCodeList}" itemValue="com02DtlCd" itemLabel="com02CodeNm"/> 
                        </form:select>                    	
                    </td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">상대대표계정코드</td>
                    <td class="text-start" colspan=2>
					<div class="input-group">
					  <button class="btn btn-outline-secondary text-warning bg-secondary btnPopupReprCode" type="button" id="btnDisplayAcntPopup"><i class="fa-solid fa-search"></i></button>
					  <form:hidden path="jnl11TgtReprAcntCd"  data-v-max-length="1" required="true"  />
					  <form:input type="text" path="jnl11TgtReprAcntNm" class="form-control" style="background-color:#F0EEEE" readonly="true" />
					</div>                    
                    </td>
                </tr>
            </table>
            
            <kfs:DisplayErrors modelAttribute="jnl11ReprAcnt"/>

            <div class="row justify-content-md-center pt-5">
                <button type="submit" class="col col-lg-2 btn btn-primary">저장</button>
                &nbsp;
                <a href="/jnl/repr_acnt/list" class="col col-lg-2 btn btn-secondary">취소, 리스트로 돌아감</a>
            </div>
        </form:form>

    </div>
</main>
<!-- =================================================== -->
<jsp:include page="../../common/footer.jsp" flush="false" />
<!-- -================================================== -->
<script type="text/javascript" src="/js/input-format.js"></script>
<script>
$(document).ready(function () {
    let validator = $('form.validcheck').jbvalidator({
        language: '/js/validation/lang/ko.json'
    });
    
    //첫번째 입력에 focus를 준다
    $("form input:text").first().focus();
	
	//form 안의 inputdp Enter key submit방지
	$('form input').on('keydown', function(e){
		if(e.keyCode == 13){
			e.preventDefault();
		}
	});
	
	$('.btnPopupReprCode').on('click', function(){
		//id 명만 주는 거임 (발행기관 코드/발행기관 이름)
		var url = '/popup/jnl/repr-acnt?reprAcntCdId=jnl11TgtReprAcntCd&reprAcntNmId=jnl11TgtReprAcntNm';
		var prop = {};
        var width = 720;
        var height = 518;
        var win = AssetUtil.popupWindow(url, '대표계정코드선택', {}, width, height);
        return false;
	});
	
	  $('form').submit(function(event) {
          var isFormValid = true;

          // data-required 속성이 있는 모든 select 요소를 검사
          $('[data-required="true"]').each(function() {
              if ($(this).val() === '') {
                  // 유효성 검사 메시지 표시
                  $(this).next('.validation-error').show();
                  isFormValid = false;
              } else {
                  // 유효성 검사 메시지 숨기기
                  $(this).next('.validation-error').hide();
              }
          });

          if (!isFormValid) {
              event.preventDefault(); // 폼 제출 방지
          }
      });
	
});
</script>
</body>
</html>