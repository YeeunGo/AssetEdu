package kr.co.kfs.assetedu.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Sys01User{
	
	@NotNull
	private String sys01UserId;  //사용자 id

	@NotNull
	private String sys01UserNm;  //사용자 이름

	@NotNull
	private String sys01tel; // 사용자 전화먼호
	
	@NotNull
	private String sys01email; // 사용자 email
	
	@NotNull	
	transient private String sys01Pwd; //사용자 password
	
}
