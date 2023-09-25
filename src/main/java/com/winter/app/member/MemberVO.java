package com.winter.app.member;

import java.sql.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString//vo 출력용도
public class MemberVO extends MemberInfoVO{

	@NotBlank
	@Size(max = 12, min = 2)
	private String username;
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\\\W)(?=\\\\S+$).{6,12}", message = "비번잘입력해")
	private String password;
	private String passwordCheck;

	private MemberInfoVO memberInfoVO;
	
	public MemberVO() {
		this.memberInfoVO = new MemberInfoVO();
	}
}
