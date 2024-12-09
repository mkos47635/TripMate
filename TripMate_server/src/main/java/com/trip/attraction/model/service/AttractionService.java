package com.trip.attraction.model.service;

import java.util.List;
import java.util.Map;

import com.trip.attraction.model.AttractionDto;

public interface AttractionService {
	
	/** 군구 별로 조회 */
	List<AttractionDto> searchAttraction(Map<String, String> map);
	List<AttractionDto> searchLikeAttraction(String userId);
	void addLikeAttraction(String userId, String contentId);
	void removeLikeAttraction(String userId, String contentId);
	List<String> getGugun(int sidoCode);
	

//	void addSido(List<SidoGugunDto> sidoGugunList);
//	void addGugun(List<SidoGugunDto> sidoGugunList);
//	void addAttraction(List<AttractionDto> attractionList);
	/** 시 + 군구 + contents type 별로 검색  (수정)*/


}
