package com.winter.app.board.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.board.BoardVO;
import com.winter.app.board.FileVO;
import com.winter.app.commons.Pager;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/notice/*")
@Slf4j
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
	
	@ModelAttribute("board")
	public String getBoard() {
		return "notice";
	}
	
	@GetMapping("fileDown")
	public String getFileDown(FileVO fileVO, Model model) throws Exception{
		fileVO = noticeService.getFileDetail(fileVO);
		model.addAttribute("fileVO", fileVO);
		return "fileDownView"; //commons fileDownView 클래스
	}
	
	@GetMapping("list")
	public String getList(Pager pager, Model model) throws Exception{
		List<BoardVO> ar = noticeService.getList(pager);
		model.addAttribute("list", ar);
		//ERROR, WARN, INFO, DEBUG, TRACE
//		log.info("getList 실행"); //찍힘 - 배포할때 찍음
//		log.debug("getList 실행"); //안찍힘 - 개발할때 찍음
//		log.warn("getList 실행"); //찍힘
		
		return "board/list";
	}
	
	@GetMapping("add")
	public String add() throws Exception{
		return "board/add";
	}
	
	@PostMapping("add")
	public String addTest(NoticeVO noticeVO, MultipartFile[] files) throws Exception{
		//log.info("files : {} ", files[0].getOriginalFilename());
		int result = noticeService.add(noticeVO, files);
		return "redirect:./list";
	}
	
	@GetMapping("detail")
	public String detail(NoticeVO noticeVO, Model model) throws Exception{
		
		BoardVO boardVO = noticeService.getDetail(noticeVO);
		model.addAttribute("boardVO", boardVO);
		
		log.info("=================");
		log.info("BoardNo : {}", boardVO.getBoardNo());
		log.info("BoardNo : {}", boardVO.getBoardTitle());
		log.info("BoardNo : {}", boardVO.getBoardWriter());
		log.info("BoardNo : {}", boardVO.getBoardNo());
		log.info("BoardNo : {}", noticeVO.getBoardNo());
		log.info("BoardNo : {}", noticeVO.getBoardTitle());
		log.info("BoardNo : {}", noticeVO.getBoardWriter());
		log.info("BoardNo : {}", noticeVO.getBoardNo());
		log.info("=================");
		return "board/detail";
	}
	
	
	@GetMapping("delete")
	public String delete(BoardVO boardVO) throws Exception{
		int result = noticeService.setDelete(boardVO);
		
		return "redirect:./list";
	}
}
