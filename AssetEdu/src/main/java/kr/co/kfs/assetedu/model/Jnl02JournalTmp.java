package kr.co.kfs.assetedu.model;

import lombok.Data;

// 임시 분개장

@Data
public class Jnl02JournalTmp {
	
	//jnl02JournalTmp
	private String 	jnl02ContId;    	//체결ID    체결내역의 체결 ID
	private Integer jnl02Seq;       	//순번      분개 매핑의 순번 13번 테이블
	private String 	jnl02DrcrType;  	//차대구분    분개 매핑 차ㅐㄷ구분 13번 테이블 
	private String 	jnl02ReprAcntCd;	//대표계정코드  분개 매핑 13번 테이블
	private Long 	jnl02Amt;     		//금액 		체결내역의 해당 금액 계산식에 맞춰서 부낵 매핑 
	
}
