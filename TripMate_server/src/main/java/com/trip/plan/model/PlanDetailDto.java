package com.trip.plan.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "PlanDetail : PlanDetail 정보", description = "PlanDetail 정보를 나타낸다.")
public class PlanDetailDto {
	@Schema(description = "일정 번호")
	private String detailId;
	@Schema(description = "계획 아이디")
	private String planId;
	@Schema(description = "계획 index")
	private String planIndex;
	@Schema(description = "일정 일수")
	private String day;
	@Schema(description = "관광지 아이디")
	private String contentId;
	@Schema(description = "관광지 제목")
	private String title;
	@Schema(description = "주소1 정보")
	private String addr1;
	@Schema(description = "주소2 정보")
	private String addr2;
	@Schema(description = "위도 정보")
	private String latitude;
	@Schema(description = "경도 정보")
	private String longitude;
}