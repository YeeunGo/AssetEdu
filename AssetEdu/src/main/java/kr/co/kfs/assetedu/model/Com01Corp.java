package kr.co.kfs.assetedu.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Com01Corp {
	@NotBlank(message="기관코드를 입력해주세요")
	private String com01CorpCd; //기관코드

	@NotBlank(message="기관구분을 입력해주세요")
	private String com01CorpType; //기관구분

	@NotBlank(message="기관명을 입력해주세요")
	private String com01CorpNm; //기관명

	private String com01CorpEnm; //기관영문명
	private String com01ExtnCorpCd; //대외기관코드
	private String com01CorpTypeNm; //기관구분명
}
