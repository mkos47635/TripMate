package com.trip.member.model.mapper;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.trip.member.model.AuthenticationRequest;
import com.trip.member.model.AuthenticationResponse;
import com.trip.member.model.MemberDto;

@Mapper
public interface MemberMapper {
	int idCheck(String userId) throws Exception;
	int joinMember(MemberDto memberDto) throws Exception;
	int modifyMember(MemberDto memberDto) throws Exception;
	int deleteMember(String userId) throws Exception;
	AuthenticationResponse loginMember(AuthenticationRequest authenticationRequest) throws Exception;
	String searchId(MemberDto memberDto) throws Exception;
	String searchPwd(MemberDto memberDto) throws Exception;
	
//	JWT
	Optional<MemberDto> getMember(String userId) throws Exception;
	Optional<String> getUserId(String userId) throws Exception;
	void saveRefreshToken(Map<String, String> map) throws SQLException;
	Object getRefreshToken(String userid) throws SQLException;
	void deleteRefreshToken(Map<String, String> map) throws SQLException;
}
