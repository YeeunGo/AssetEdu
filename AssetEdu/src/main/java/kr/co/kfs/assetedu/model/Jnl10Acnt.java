package kr.co.kfs.assetedu.model;

import lombok.Data;

@Data
public class Jnl10Acnt {
	private String jnl10AcntCd;      //계정코드               
	private String jnl10AcntNm;      //계정명                
	private String jnl10ParentCd;    //상위계정코드    
	private String jnl10ParentNm;    //상위계정코드명   (다른곳에서 가져옴)
	private String jnl10AcntAttrCd; //계정속성(AcntAttrCode) 
	private String jnl10AcntAttrNm; //계정속성(AcntAttrCode)명 (다른 곳)
	private String jnl10DrcrType;    //차대구분(DrcrType)     
	private String jnl10DrcrTypeNm;    //차대구분(DrcrType)명칭   (다른 곳) 
	private String jnl10SlipYn;      //전표생성여부             
	private String jnl10UseYn;       //사용여부
}
