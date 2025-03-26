package com.trip.plan.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.trip.plan.model.PlanDetailDto;
import com.trip.plan.model.PlanDto;
import com.trip.plan.model.PlanInviteResponseDto;

@Mapper
public interface PlanMapper {
	List<PlanDto> getPlanList(String userId);

	List<PlanDetailDto> getPlanDetailList(String planId);

	int checkExistence(PlanDetailDto planDetailDto);

	void insertPlanDetail(PlanDetailDto planDetailDto);

	void updateIndex(PlanDetailDto planDetailDto);

	void deletePlanDetail(String contentId, String day);

	void deletePlan(int planId);

	PlanInviteResponseDto getPlanById(int planId);

	void savePlanForShare(int planId, String userId);

	void savePlan(PlanDto planner);
}
