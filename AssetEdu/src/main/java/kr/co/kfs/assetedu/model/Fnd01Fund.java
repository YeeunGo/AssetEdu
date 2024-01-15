package kr.co.kfs.assetedu.model;

import javax.validation.constraints.NotBlank;

import kr.co.kfs.assetedu.servlet.converter.YmdFormat;
import lombok.Data;

@Data
public class Fnd01Fund {
	@NotBlank
	String fnd01FundCd; 		 // 펀드코드
	@NotBlank
	String fnd01FundNm; 		 // 펀드명
	
	String fnd01FundType; 		 // 펀드유형
	String fnd01FundTypeNm;
	
	String fnd01PublicCd; 		 // 공모/사모
	String fnd01PublicCdNm;
	
	String fnd01UnitCd; 	     // 단위
	String fnd01UnitCdNm;
	
	String fnd01ParentCd;  		 // 모/자
	String fnd01ParentCdNm;
	
	String fnd01ParentFundCd; 	 // 모펀드 코드
	String fnd01ParentFundCdNm;
	
	@YmdFormat("-")
	String fnd01StartDate;		 // 설정일자
	@YmdFormat
	String fnd01EndDate;		 // 해지일자
	Integer fnd01AccPeriod; 	 // 회계기간
	@YmdFormat
	String fnd01FirstCloseDate;	 // 최초결산일자
	String fnd01CurCd;			 // 기준통화
	String fnd01KsdItemCd; 		 // 예탁원종목코드
	String fnd01KofiaCd;		 // 협회표준코드
	String fnd01KofiaClassCd;    // 협회상품분류코드
	String fnd01FssCd;			 // 금감원펀드코드
	String fnd01Manager;		 // 주운용역
	String fnd01SubManager;		 // 부운용역
	
	String fnd01MngCoCd;		 // 운용사코드
	String fnd01MngCoCdNm;
	
	String fnd01TrustCoCd; 		 // 수탁사코드
	String fnd01TrustCoCdNm;
	
	String fnd01OfficeCoCd;		 // 사무관리사(주)코드
	String fnd01OfficeCoCdNm;
	
	String fnd01SubOfficeCoCd;	 // 사무관리사(부)코드
}
