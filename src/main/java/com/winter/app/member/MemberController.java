package com.winter.app.member;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member/*")
@Slf4j
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("update")
	public void setUpdate(HttpSession session, Model model) throws Exception{
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		memberVO = memberService.getLogin(memberVO);
		model.addAttribute("memberVO", memberVO);
	}
	
	@PostMapping("update")
	public void setUpdate(@Valid MemberVO memberVO, BindingResult bindingResult) throws Exception{
		
	}
	
	@GetMapping("logout")
	public String getLogout(HttpSession session) throws Exception{
		session.invalidate();
		
		return "redirect:../";
	}
	
	@GetMapping("login")
	public void getLogin(@ModelAttribute MemberVO memberVO) throws Exception{
		
	}
	
	@PostMapping("login")
	public String getLogin2(MemberVO memberVO, HttpSession session) throws Exception{
		memberVO = memberService.getLogin(memberVO);
		if(memberVO != null) {
			session.setAttribute("member", memberVO);
			return "redirect:../";
		}
		
		return "redirect:./login";
	}

//	@GetMapping("join")
//	public void setJoin(Model model) throws Exception{
//		MemberVO memberVO = new MemberVO();
//		model.addAttribute("memberVO", memberVO);
//	}
	
	@GetMapping("join")
	public void setJoin(@ModelAttribute MemberVO memberVO) throws Exception{
			
	}
	
	@PostMapping("join")
	public String setJoin(@Valid MemberVO memberVO, BindingResult bindingResult, MultipartFile photo) throws Exception{
		
		boolean check = memberService.getMemberError(memberVO, bindingResult);
		
		if(bindingResult.hasErrors() || check) {
			return "member/join";
		}
		
		//회원가입 진행
		
		log.info("photo : {} --- size : {}", photo.getOriginalFilename(), photo.getSize());
		return "redirect:../";
	}
}
