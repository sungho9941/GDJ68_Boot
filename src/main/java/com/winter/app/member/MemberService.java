package com.winter.app.member;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService extends DefaultOAuth2UserService implements UserDetailsService{
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//social login
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		log.info("====== Social Login 처리 진행 ======");
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		log.info("====== {} ======", clientRegistration);
		
		OAuth2User auth2User = super.loadUser(userRequest); // 카톡 실제 유저정보
		log.info("===== Auth2User : {} =====", auth2User);
		
		String social = clientRegistration.getRegistrationId(); //클라이언트id(ex.카카오, 네이버)
		if(social.equals("kakao")) {
			auth2User = this.forKakao(auth2User);
		}
		return auth2User;
	}
	
	private OAuth2User forKakao(OAuth2User auth2User){
		MemberVO memberVO = new MemberVO();
		LinkedHashMap<String, String> map = auth2User.getAttribute("properties");
		//{nickname=김성호, profile_image=http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg, thumbnail_image=http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg}
		log.info("****** {} ******", auth2User.getAttribute("properties").toString()); // getClass -> 리턴타입 찾기위해..
		
		LinkedHashMap<String, Object> kakaoAccount = auth2User.getAttribute("kakao_account");
		
		LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>)kakaoAccount.get("profile");
		
		
		
		log.info("NickName : {} ====", profile.get("nickname"));
		log.info("ProfileImange : {} ====", profile.get("profile_image_url"));
		log.info("Email : {} ====", kakaoAccount.get("email"));
		log.info("Birth : {} ====", kakaoAccount.get("birthday"));
		
		String birth = kakaoAccount.get("birthday").toString(); // 0310
		String m = birth.substring(0, 2);
		String d = birth.substring(2);
		Calendar ca = Calendar.getInstance();
		int y = ca.get(Calendar.YEAR);
		StringBuffer sb = new StringBuffer();
		sb.append(y);
		sb.append("-");
		sb.append(m);
		sb.append("-");
		sb.append(d);
		
		memberVO.setUsername(map.get("nickname"));
		memberVO.setName(map.get("nickname"));
		memberVO.setEmail(kakaoAccount.get("email").toString());
		memberVO.setBirth(Date.valueOf(sb.toString()));
		

		memberVO.setAttributes(auth2User.getAttributes());
		
		List<RoleVO> list = new ArrayList<>();
		RoleVO roleVO = new RoleVO();
		roleVO.setRoleName("ROLE_MEMBER");
		
		list.add(roleVO);
		
		memberVO.setRoleVOs(list);
		
		log.info("Date : {} ====", Date.valueOf(sb.toString()));

		return memberVO;
	}
	
	//server login
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		log.info("====== 로그인 시도 중 ==========");
		MemberVO memberVO = new MemberVO();
		memberVO.setUsername(username);
		try {
			memberVO = memberDAO.getMember(memberVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			memberVO=null;
		}
		return memberVO;
	}
	

	//검증메서드
	public boolean getMemberError(MemberVO memberVO, BindingResult bindingResult) throws Exception {
		boolean check=false; //error가 없다, true : error가 있다. 검증실패
		
		//password 일치 검증
		if(!memberVO.getPassword().equals(memberVO.getPasswordCheck())) {
			check=true; //check=!check
			
			bindingResult.rejectValue("passwordCheck", "memberVO.password.equalCheck");
		}
		
		// ID 중복 검사
		MemberVO checkVO = memberDAO.getMember(memberVO);
		
		if(checkVO != null) {
			check = true;
			bindingResult.rejectValue("username", "memberVO.username.equalCheck");
		}
		
		
		return check;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int setJoin(MemberVO memberVO) throws Exception{
		memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
		int result = memberDAO.setJoin(memberVO);
		Map<String, Object> map = new HashMap<>();
		map.put("roleNum", 3);
		map.put("username", memberVO.getUsername());
		result = memberDAO.setMemberRole(map);
		return result;
	}
	
}
