package com.trip.attraction.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.trip.attraction.model.AttractionDto;

@Mapper
public interface AttractionMapper {
	
	/** 군구 별로 조회 */
	List<AttractionDto> searchAttraction(Map<String, String> map);
	List<AttractionDto> searchLikeAttraction(String userId);
	void addLikeAttraction(String userId, String contentId);
	void removeLikeAttraction(String userId, String contentId);
	List<String> getGugun(int sidoCode);
	
}