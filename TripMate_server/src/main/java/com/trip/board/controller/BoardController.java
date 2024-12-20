package com.trip.board.controller;

import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.board.model.BoardDto;
import com.trip.board.model.BoardListDto;
import com.trip.board.model.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "게시판 컨트롤러", description = "게시판에 글을 등록, 수정, 삭제, 목록, 상세보기등 전반적인 처리를 하는 클래스.")
@RequestMapping("/board")
public class BoardController {
	
	private final BoardService boardService;
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@Operation(summary = "게시판 글작성", description = "새로운 게시글 정보를 입력한다.")
	@PostMapping
	public ResponseEntity<?> write(
			@RequestBody @Parameter(description = "작성글 정보.", required = true) BoardDto boardDto, 
			@AuthenticationPrincipal UserDetails userDetails) throws Exception {
		try {
			// Authorization 헤더에서 JWT 토큰 추출 (Bearer <token>)
			String userId = userDetails.getUsername();
			log.debug("write boardDto : {}", boardDto);
			boardDto.setUserId(userId);
			boardService.writeArticle(boardDto);
//			return ResponseEntity.ok().build();
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	@Operation(summary = "게시판 글목록", description = "모든 게시글의 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "게시글목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping
	public ResponseEntity<?> listArticle(
			@RequestParam @Parameter(description = "게시글을 얻기위한 부가정보.", required = true) Map<String, String> map) {
		log.info("listArticle map - {}", map);
		try {
			BoardListDto boardListDto = boardService.listArticle(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(boardListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "게시판 글보기", description = "글번호에 해당하는 게시글의 정보를 반환한다.")
	@GetMapping("/{articleNo}")
	public ResponseEntity<BoardDto> getArticle(
			@PathVariable("articleNo") @Parameter(name = "articleNo", description = "얻어올 글의 글번호.", required = true) int articleNo)
			throws Exception {
		log.info("getArticle - 호출 : " + articleNo);
		boardService.updateHit(articleNo);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleNo), HttpStatus.OK);
	}

	@Operation(summary = "수정 할 글 얻기", description = "글번호에 해당하는 게시글의 정보를 반환한다.")
	@GetMapping("/modify/{articleNo}")
	public ResponseEntity<BoardDto> getModifyArticle(
			@PathVariable("articleNo") @Parameter(name = "articleNo", description = "얻어올 글의 글번호.", required = true) int articleNo)
			throws Exception {
		log.info("getModifyArticle - 호출 : " + articleNo);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleNo), HttpStatus.OK);
	}

	@Operation(summary = "게시판 글수정", description = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@PutMapping
	public ResponseEntity<String> modifyArticle(
			@RequestBody @Parameter(description = "수정할 글정보.", required = true) BoardDto boardDto) throws Exception {
		log.info("modifyArticle - 호출 {}", boardDto);
		boardService.modifyArticle(boardDto);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "게시판 글삭제", description = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@DeleteMapping("/{articleNo}")
	public ResponseEntity<String> deleteArticle(@PathVariable("articleNo") @Parameter(name = "articleNo", description = "살제할 글의 글번호.", required = true) int articleNo) throws Exception {
		log.info("deleteArticle - 호출");
		boardService.deleteArticle(articleNo);
		return ResponseEntity.ok().build();
	}

//	@GetMapping("/download")
//	public ModelAndView downloadFile(@RequestParam("sfolder") String sfolder, @RequestParam("ofile") String ofile,
//			@RequestParam("sfile") String sfile) {
//		Map<String, Object> fileInfo = new HashMap<String, Object>();
//		fileInfo.put("sfolder", sfolder);
//		fileInfo.put("ofile", ofile);
//		fileInfo.put("sfile", sfile);
//		return new ModelAndView("fileDownLoadView", "downloadFile", fileInfo);
//	}
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
