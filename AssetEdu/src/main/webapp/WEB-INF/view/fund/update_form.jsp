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
<jsp:include page="../common/meta_css.jsp" flush="false" />
<!-- =================================================== -->
<title><c:out value="${pageTitle}" default="펀드정보-등록" /></title>
</head>
<body>
<!-- =================================================== -->
<jsp:include page="../common/header.jsp" flush="false" />
<!-- =================================================== -->
<main class="container mx-3 my-3">

	<h2><i class="fa-solid fa-cube my-3"></i> 펀드정보관리 > 펀드정보 상세/수정</h2>
    <div class="border-top border-2 p-4">

        <form:form action="/fund/update" method="POST" modelAttribute="fund" class="validcheck" >
            <table class="table table-sm table-borderless">
                <tr class="align-middle">
                    <td class="text-end" style="width:130px">펀드코드/명</td>
                    <td class="w-25"><form:input type="text" class="form-control" path="fnd01FundCd" data-v-max-length="4" data-v-min-length="4" required="true" placeholder="필수입력(4자리)" readonly="true"/></td>
                    <td colspan="4"><form:input type="text" class="form-control" path="fnd01FundNm" required="true" placeholder="필수입력"/></td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">예탁원코드</td>
                    <td class="w-25"><form:input type="text" class="form-control" path="fnd01KsdItemCd" data-v-max-length="12" placeholder="입력"/></td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end">설정일자</td>
                    <td><form:input type="date" class="form-control w-50 dateinput" path="fnd01StartDate" placeholder="YYYY-MM-DD" /></td>
                    <td style="width:50px">&nbsp;</td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">협회표준코드</td>
                    <td class="w-25"><form:input type="text" class="form-control" path="fnd01KofiaCd" data-v-max-length="12" placeholder="입력"/></td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end">해지일자</td>
                    <td><form:input type="date" class="form-control w-50 dateinput" path="fnd01EndDate"  placeholder="YYYY-MM-DD"/></td>
                    <td style="width:50px">&nbsp;</td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">금감원코드</td>
                    <td class="w-25"><form:input type="text" class="form-control" path="fnd01FssCd" data-v-max-length="12"  placeholder="입력"/></td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end">최초결산일자</td>
                    <td><form:input type="date" class="form-control w-50 dateinput" path="fnd01FirstCloseDate"  placeholder="YYYY-MM-DD"/></td>
                    <td style="width:50px">&nbsp;</td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">펀드유형</td>
                    <td class="w-25">
                        <form:select path="fnd01FundType" class="form-select">
                            <form:option value="" label="선택"/>
                            <form:options items="${fundTypeList}" itemValue="com02DtlCd" itemLabel="com02CodeNm"/> 
                        </form:select>
                    </td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end">회계기간</td>
                    <td><form:input type="number" class="form-control w-25 text-end d-inline" path="fnd01AccPeriod" placeholder="입력"/>
                        <span class="d-inline">개월</span></td>
                    <td style="width:50px">&nbsp;</td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">공모/사모</td>
                    <td class="w-25">
                        <form:select path="fnd01PublicCd" class="form-select">
                            <form:option value="" label="선택"/>
                            <form:options items="${publicCdList}" itemValue="com02DtlCd" itemLabel="com02CodeNm"/> 
                        </form:select>
                    </td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end">주운용역</td>
                    <td><form:input type="text" class="form-control" path="fnd01Manager" placeholder="입력"/></td>
                    <td style="width:50px">&nbsp;</td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">펀드단위</td>
                    <td class="w-25">
                        <form:select path="fnd01UnitCd" class="form-select">
                            <form:option value="" label="선택"/>
                            <form:options items="${unitCdList}" itemValue="com02DtlCd" itemLabel="com02CodeNm"/> 
                        </form:select>
                    </td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end">부운용역</td>
                    <td><form:input type="text" class="form-control" path="fnd01SubManager" placeholder="입력"/></td>
                    <td style="width:50px">&nbsp;</td>
                </tr>
                <tr class="align-middle">
                    <td class="text-end">모자구분</td>
                    <td class="w-25">
                        <form:select path="fnd01ParentCd" class="form-select"  onchange="checkParentCd()">
                            <form:option value="" label="선택"/>
                            <form:options items="${parentCdList}" itemValue="com02DtlCd" itemLabel="com02CodeNm"/> 
                        </form:select>                    
                    </td>
                    <td style="width:50px">&nbsp;</td>
                    <td class="text-end w-25">운용사</td>
                    <td>
                        <form:hidden path="fnd01MngCoCd" />
                        <form:input type="text" class="form-control bg-light" path="fnd01MngCoCdNm" readonly="true" />
                    </td>
                    <td><button class="btn btn-primary" id="btnPopupMngCo"><i class="fa-solid fa-search"></i></button></td>
                </tr>
                <tr class="align-middle" >
                    <!-- 자펀드가 아니면 보이지 않게 설정 OR 비활성화 -->
                    <td class="text-end">모펀드</td>
                    <td>
                        <form:hidden path="fnd01ParentFundCd" />
                        <form:input type="text" class="form-control bg-light" path="fnd01ParentFundCdNm" readonly="true" />
                    </td>
                    <td><button class="btn btn-primary" id="btnPopupFund"><i class="fa-solid fa-search"></i></button></td>

                    <td class="text-end">수탁사</td>
                    <td>
                        <form:hidden path="fnd01TrustCoCd" />
                        <form:input type="text" class="form-control bg-light" path="fnd01TrustCoCdNm" readonly="true" />
                    </td>
                    <td><button class="btn btn-primary" id="btnPopupTrustCo"><i class="fa-solid fa-search"></i></button></td>
                </tr>
                <tr class="align-middle">
                    <td colspan=3></td>
                    <td class="text-end">사무수탁사</td>
                    <td>
                        <form:hidden path="fnd01OfficeCoCd" />
                        <form:input type="text" class="form-control bg-light" path="fnd01OfficeCoCdNm" readonly="true"/>
                    </td>
                    <td><button class="btn btn-primary" id="btnPopupOfficeCo"><i class="fa-solid fa-search"></i></button></td>
                </tr>
            </table>
            
            <kfs:DisplayErrors modelAttribute="fund"/>
            
            <div class="row justify-content-md-center pt-5">
                <button type="submit" class="btn btn-primary w-25 mx-1">저장</button>
                <a href="/fund/list" class="btn btn-secondary w-25">취소, 리스트로 돌아감</a>
            </div>

        </form:form>

    </div>
</main>
<!-- =================================================== -->
<jsp:include page="../common/footer.jsp" flush="false" />
<!-- -================================================== -->
<script type="text/javascript" src="/js/input-format.js"></script>
<script>
$(document).ready(function () {
	checkParentCd();
	//validation
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
	
	$('#btnPopupMngCo').on('click', function(){
		//id 명만 주는 거임 (발행기관 코드/발행기관 이름)
		var url = '/popup/corp?corpCd=fnd01MngCoCd&corpNm=fnd01MngCoCdNm&whichPage=asset';
		var prop = {};
        var width = 720;
        var height = 518;
        var win = AssetUtil.popupWindow(url, '운용사선택', {}, width, height);
        return false;
	});
	
	$('#btnPopupTrustCo').on('click', function(){
		//id 명만 주는 거임 (발행기관 코드/발행기관 이름)
		var url = '/popup/corp?corpCd=fnd01TrustCoCd&corpNm=fnd01TrustCoCdNm&whichPage=custodial';
		var prop = {};
        var width = 720;
        var height = 518;
        var win = AssetUtil.popupWindow(url, '수탁사선택', {}, width, height);
        return false;
	});
	
	$('#btnPopupOfficeCo').on('click', function(){
		//id 명만 주는 거임 (발행기관 코드/발행기관 이름)
		var url = '/popup/corp?corpCd=fnd01OfficeCoCd&corpNm=fnd01OfficeCoCdNm&whichPage=Adminis';
		var prop = {};
        var width = 720;
        var height = 518;
        var win = AssetUtil.popupWindow(url, '사무수탁사선택', {}, width, height);
        return false;
	});
	
	$('#btnPopupFund').on('click', function(){
		//id 명만 주는 거임 (발행기관 코드/발행기관 이름)
		var url = '/popup/fund?fundCd=fnd01OfficeCoCd&fundNm=fnd01OfficeCoCdNm&whatType=mofund';
		var prop = {};
        var width = 720;
        var height = 518;
        var win = AssetUtil.popupWindow(url, '사무수탁사선택', {}, width, height);
        return false;
	});
	
});

function checkParentCd() {
    var selectedValue = document.getElementById("fnd01ParentCd").value;
    var Element = document.getElementById("btnPopupFund");
    if(selectedValue === "3") {
    	Element.style.display = "block";
    } else {
    	Element.style.display = "none";
    }
}
</script>
</body>
</html>