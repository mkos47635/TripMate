package com.trip.member.model.service;

import java.util.Optional;

import com.trip.member.model.AuthenticationRequest;
import com.trip.member.model.AuthenticationResponse;
import com.trip.member.model.MemberDto;

public interface MemberService {

	int idCheck(String userId) throws Exception;
	int joinMember(MemberDto memberDto) throws Exception;
	int modifyMember(MemberDto memberDto) throws Exception;
	int deleteMember(String userId) throws Exception;
	AuthenticationResponse loginMember(AuthenticationRequest authenticationRequest) throws Exception;
	String searchId(MemberDto memberDto) throws Exception;
	String searchPwd(MemberDto memberDto) throws Exception;
	Optional<MemberDto> getMember(String userId) throws Exception;
//	JWT
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
}
