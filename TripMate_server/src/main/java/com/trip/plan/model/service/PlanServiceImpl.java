package com.trip.plan.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.trip.plan.model.PlanInviteResponseDto;
import com.trip.plan.model.PlanDetailDto;
import com.trip.plan.model.PlanDto;
import com.trip.plan.model.mapper.PlanMapper;

@Service
public class PlanServiceImpl implements PlanService {

	private final PlanMapper planMapper;
	private final ShortestDistanceService shortestDistanceService;

	public PlanServiceImpl(PlanMapper planMapper, ShortestDistanceService shortestDistanceService) {
		this.planMapper = planMapper;
		this.shortestDistanceService = shortestDistanceService;
	}

	@Override
	public List<PlanDto> getPlanList(String userId) {
		return planMapper.getPlanList(userId);
	}

	@Override
	public List<PlanDetailDto> getPlanDetailList(String planId) {
		return planMapper.getPlanDetailList(planId);
	}

	@Override
	public int checkExistence(PlanDetailDto planDetailDto) {
		return planMapper.checkExistence(planDetailDto);
	}

	@Override
	public void insertPlanDetail(PlanDetailDto planDetailDto) {
		planMapper.insertPlanDetail(planDetailDto);
	}

	@Override
	public void updateIndex(PlanDetailDto planDetailDto) {
		planMapper.updateIndex(planDetailDto);
	}

	@Override
	public void deletePlanDetail(String contentId, String day) {
		planMapper.deletePlanDetail(contentId, day);
	}

	@Override
	public List<Map<String, Object>> getShortestDistance(List<Map<String, Object>> items) {
		return shortestDistanceService.getShortestDistance(items);
	}

	public void deletePlan(int planId) {
		planMapper.deletePlan(planId);
	}

	@Override
	public PlanInviteResponseDto getPlanById(String planId) {
		return planMapper.getPlanById(Integer.parseInt(planId));
	}

	@Override
	public void savePlanForShare(String planId, String userId) {
		planMapper.savePlanForShare(Integer.parseInt(planId), userId);

	}

	@Override
	public void savePlanner(PlanDto planner) {
		planMapper.savePlan(planner);

	}
}
