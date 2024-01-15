package kr.co.kfs.assetedu.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Jnl12Tr {
	@NotNull(message="필수입력란입니다.")
	private String  jnl12TrCd;
	private String  jnl12TrNm;
	
	private String  jnl12InOutType; 	 //원장입출고구분
	private String  jnl12InOutTypeNm; //원장 입출고 구분명
	
	private String  jnl12UsePageType;
	private String  jnl12UseYn;
	
	private String originTrCd;
}
