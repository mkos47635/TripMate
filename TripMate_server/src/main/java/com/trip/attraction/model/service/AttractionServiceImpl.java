package com.trip.attraction.model.service;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.trip.attraction.model.AttractionDto;
import com.trip.attraction.model.mapper.AttractionMapper;

@Service
public class AttractionServiceImpl implements AttractionService{
	
	private final AttractionMapper attractionMapper;

	public AttractionServiceImpl(AttractionMapper attractionMapper) {
		this.attractionMapper = attractionMapper;
	}

	
	@Override
	public List<AttractionDto> searchAttraction(Map<String, String> map) {
		return attractionMapper.searchAttraction(map);
	}

	@Override
	public List<AttractionDto> searchLikeAttraction(String userId) {
		return attractionMapper.searchLikeAttraction(userId);
	}



	@Override
	public void addLikeAttraction(String userId, String contentId) {
		attractionMapper.addLikeAttraction(userId, contentId);		
	}



	@Override
	public void removeLikeAttraction(String userId, String contentId) {
		attractionMapper.removeLikeAttraction(userId, contentId);
		
	}


	@Override
	public List<String> getGugun(int sidoCode) {
		return attractionMapper.getGugun(sidoCode);
	}
	
	

}
