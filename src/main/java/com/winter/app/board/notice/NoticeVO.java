package com.winter.app.board.notice;

import java.sql.Date;
import java.util.List;

import com.winter.app.board.BoardVO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeVO extends BoardVO{
	
	private List<NoticeFileVO> list;
	
}
