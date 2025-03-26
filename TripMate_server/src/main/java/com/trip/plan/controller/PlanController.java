package com.trip.plan.controller;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import com.trip.plan.model.PlanInviteResponseDto;

import com.trip.plan.model.PlanDetailDto;
import com.trip.plan.model.PlanDto;
import com.trip.plan.model.service.PlanService;
import com.trip.util.EncryptionUtil;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {
	
	private final PlanService planService;
	private final EncryptionUtil encryptionUtil;
	
	@Operation(summary = "PlanDetailList 가져오기", description = "planId를 통해 생성된 여행 일정들을 반환한다.")
	@GetMapping("/detail")
	public ResponseEntity<?> getPlanDetailList(@RequestParam("planId") String planId) {
		log.info("선택된 planId: ", planId);
	    try {
	        List<PlanDetailDto> planDetailList = planService.getPlanDetailList(planId);
	        HttpHeaders header = new HttpHeaders();
	        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
	        return ResponseEntity.ok().headers(header).body(planDetailList);
	    } catch (Exception e) {
	        return exceptionHandling(e);
	    }
	}
    @PostMapping("/create")
    public ResponseEntity<?> createPlanner(@RequestBody PlanDto planner) {
	    try {
	        planService.savePlanner(planner);
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        return exceptionHandling(e);
	    }
    }
	@Operation(summary = "Plan 삭제하기", description = "planId를 통해 지정된 여행 일정을 삭제한다.")
	@DeleteMapping("/delete/{planId}")
	public ResponseEntity<?> deletePlan(@PathVariable int planId) {
		log.info("선택된 planId: ", planId);
	    try {
	        planService.deletePlan(planId);
	        HttpHeaders header = new HttpHeaders();
	        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        return exceptionHandling(e);
	    }
	}
	@GetMapping("/share/{planId}")
	public ResponseEntity<?> generateShareLink(@PathVariable String planId) {
	    try {
	        // Plan ID 암호화
	        String encryptedId = encryptionUtil.encrypt(planId);
	        // 암호화된 ID를 URL-safe하게 인코딩
	        String encodedId = Base64.getUrlEncoder().encodeToString(encryptedId.getBytes());
	        // Plan 정보 가져오기
	        PlanInviteResponseDto planInviteResponse = planService.getPlanById(planId);
	        if (planInviteResponse == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found");
	        }
	        
	        String planTitle = encryptionUtil.encode(planInviteResponse.getTitle());
	        String userName = encryptionUtil.encode(planInviteResponse.getUserName()); // 유저 이름
	        // URL 생성
	        // 초대 링크 생성
	        String shareLink = "http://localhost:5173/plan/invite?encryptedId=" + encodedId +
	                "&planTitle=" + planTitle +
	                "&userName=" + userName;

	        return ResponseEntity.ok(shareLink);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating share link");
	    }
	}
    
    // 수락 시 Plan 데이터 저장 및 리다이렉트
    @PostMapping("/accept")
    public ResponseEntity<?> acceptPlan(
            @RequestBody Map<String, String> request) {
        try {
            // 복호화
            String decodedId = new String(Base64.getUrlDecoder().decode(request.get("encryptedId")));
            String planId = encryptionUtil.decrypt(decodedId);
            // URL 디코딩
            planService.savePlanForShare(planId, request.get("userId"));

            // Plan 페이지로 리다이렉트
            return ResponseEntity.ok()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing plan");
        }
    }


	@Operation(summary = "Plan Details 저장", description = "여행 계획 세부 사항을 저장합니다.")
	@PostMapping("/save")
	public ResponseEntity<?> savePlanDetails(@RequestBody PlanDetailDto planDetailDto) {
	    log.info("저장할 데이터: {}", planDetailDto);
	    System.out.println(11111111);
	    System.out.println(11111111);
	    System.out.println(11111111);
	   
	    try {
	 	    // 존재유무 확인 후 존재하지않으면 추가 / 존재하면 index 변경
	 	    int check = planService.checkExistence(planDetailDto);
	 	    System.out.println(check);
	 	    System.out.println(planDetailDto.getContentId() +" " + planDetailDto.getDay());
	 	    if(check>0) {
	 	    	System.out.println(22222222);
	 	    	planService.updateIndex(planDetailDto);
	 	    }
	 	    else {
	 	    	System.out.println(123213);
	 	    	planService.insertPlanDetail(planDetailDto);
	 	    }
	    	return new ResponseEntity<Void>(HttpStatus.CREATED);
	    } catch (Exception e) {
	        log.error("저장 실패 : ", e);
	        return exceptionHandling(e);
	    }
	}
	
	@Operation(summary = "Plan Details 삭제", description = "여행 계획 세부 사항을 삭제합니다.")
	@PostMapping("/delete")
	public ResponseEntity<?> deletePlanDetails(@RequestBody PlanDetailDto planDetailDto) {
	    log.info("저장할 데이터: {}", planDetailDto);
	   
	    try {
	    	planService.deletePlanDetail(planDetailDto.getContentId(), planDetailDto.getDay());
	    	return new ResponseEntity<Void>(HttpStatus.CREATED);
	    } catch (Exception e) {
	        log.error("삭제 실패 : ", e);
	        return exceptionHandling(e);
	    }
	}

    @Operation(summary = "PlanList 가져오기", description = "userId를 통해 생성된 여행 계획들을 반환한다.")
    @GetMapping("/list")
    public ResponseEntity<?> getPlanList(@RequestParam("userId") String userId) {
        log.info("선택된 userId: {}", userId); // '{}' 추가
        try {
            List<PlanDto> planList = planService.getPlanList(userId);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            return ResponseEntity.ok().headers(header).body(planList);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }


    @Operation(summary = "Plan navigation post", description = "여행 계획 아이템 리스트를 받아 처리합니다.")
    @PostMapping("/send")
    public ResponseEntity<?> sendPlanItems(@RequestBody List<Map<String, Object>> items) {
        log.info("받은 데이터: {}", items);
        try {
            List<Map<String, Object>> shortestDistance = planService.getShortestDistance(items);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            return ResponseEntity.ok().headers(header).body(shortestDistance);
        } catch (Exception e) {
            log.error("데이터 처리 실패 : ", e);
            return exceptionHandling(e);
        }
    }

    private ResponseEntity<String> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
