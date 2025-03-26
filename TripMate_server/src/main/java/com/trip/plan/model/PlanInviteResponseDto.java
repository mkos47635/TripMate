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
@Schema(title = "Plan : Plan 정보", description = "유저가 초대한 Plan 정보를 나타낸다.")
public class PlanInviteResponseDto {
	@Schema(description = "계획 제목")
	private String title;
	@Schema(description = "유저 아이디")
	private String userName;
}
