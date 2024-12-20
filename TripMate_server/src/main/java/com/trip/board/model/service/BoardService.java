package com.trip.board.model.service;

import java.util.Map;

import com.trip.board.model.BoardDto;
import com.trip.board.model.BoardListDto;

public interface BoardService {

	void writeArticle(BoardDto boardDto) throws Exception;
	BoardListDto listArticle(Map<String, String> map) throws Exception;
//	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	BoardDto getArticle(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	
	void modifyArticle(BoardDto boardDto) throws Exception;
//	void deleteArticle(int articleNo, String path) throws Exception;
	void deleteArticle(int articleNo) throws Exception;
}
