package kr.co.kfs.assetedu.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

import lombok.Data;
/**
 * 종목정보
 */
@Data
public class Itm01Item {
               
	@Size(min = 12, max = 12, message = "종목코드는 정확히 12자여야 합니다.")
	private String itm01ItemCd;     //종목코드                        
	private String itm01ItemNm;     //종목명                         
	private String itm01ItemEnm;    //영문명                         
	private String itm01ShortCd;    //단축코드                        
	private String itm01IssType;    //발행구분(IssType:공모/사모)         
	private String itm01StkType;    //증권종류(StkType:보통주/우선주/..)    
	
	@NotBlank(message ="필수입력란 입니다.")
	private String itm01ListType;   //상장구분(ListType:상장/비상장/상장폐지)  
	private String itm01MarketType; //시장구분(MarketType:코스피/코스닥/..) 

	@NumberFormat(pattern = "#,###")
	private Long   itm01Par;         //액면가                         

	private String itm01IssCoCd;   //발행기관코드
	
	private String OriginCd;
	
	private String itm01IssTypeNm;    //발행구분명         
	private String itm01StkTypeNm;    //증권종류명    
	private String itm01ListTypeNm;   //상장구분명  
	private String itm01MarketTypeNm; //시장구분명 
	private String itm01IssCoNm;   	  //발행기관명
}
