package kr.co.kfs.assetedu.model;

import lombok.Data;

@Data
public class Jnl11ReprAcnt  {
	//대표계정코드 model
	String jnl11ReprAcntCd;
	String jnl11ReprAcntNm;
	String jnl11AcntAttributeCd; // 계정 속성 코드
	String jnl11AcntAttributeNm; // 계정 속성 명 (Code table)
	String jnl11TgtReprAcntCd;
	String jnl11TgtReprAcntNm; // 상대대표계정 명
}
