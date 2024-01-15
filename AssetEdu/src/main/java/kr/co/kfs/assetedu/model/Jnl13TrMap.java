package kr.co.kfs.assetedu.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Jnl13TrMap {
	@NotNull(message="필수 입력란 입니다.")
	private String jnl13TrCd;
	@NotNull(message="필수 입력란 입니다.")
	private Integer jnl13Seq;
	
	private String jnl13ReprAcntCd; // 대표계정 코드
	private String jnl11ReprAcntNm; // 대표계정명 (join)
	
	private String jnl13DrcrType; //차대 구분
	private String jnl13DrcrTypeNm; // 차대 구분 명(f_com_cd)
	private String jnl13Formula;
	
	private Integer originTrSeq;
	
	
}
