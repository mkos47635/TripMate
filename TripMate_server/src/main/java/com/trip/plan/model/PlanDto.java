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
@Schema(title = "Plan : Plan 정보", description = "유저가 생성한 Plan 정보를 나타낸다.")
public class PlanDto {
	@Schema(description = "계획 번호")
	private int planId;
	@Schema(description = "유저 아이디")
	private String userId;
	@Schema(description = "계획 제목")
	private String title;
	@Schema(description = "계획 설명")
	private String description;
	@Schema(description = "이미지 정보")
	private String image;
	@Schema(description = "계획 일수")
	private int tourDay;
	private String startDate;
}
