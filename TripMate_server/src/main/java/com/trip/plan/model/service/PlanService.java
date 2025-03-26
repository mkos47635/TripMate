package com.trip.plan.model.service;

import java.util.List;
import java.util.Map;

import com.trip.plan.model.PlanInviteResponseDto;
import com.trip.plan.model.PlanDetailDto;
import com.trip.plan.model.PlanDto;

public interface PlanService {
	List<PlanDto> getPlanList(String userId);

	List<PlanDetailDto> getPlanDetailList(String planId);

	int checkExistence(PlanDetailDto planDetailDto);

	void insertPlanDetail(PlanDetailDto planDetailDto);

	void updateIndex(PlanDetailDto planDetailDto);

	void deletePlanDetail(String contentId, String day);

	void deletePlan(int planId);

	PlanInviteResponseDto getPlanById(String planId);

	void savePlanForShare(String planId, String userId);

	void savePlanner(PlanDto planner);
	
	List<Map<String, Object>> getShortestDistance(List<Map<String, Object>> items);
}
