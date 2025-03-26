package com.trip.member.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trip.member.model.AuthenticationRequest;
import com.trip.member.model.AuthenticationResponse;
import com.trip.member.model.MemberDto;
import com.trip.member.model.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
    
	private final MemberMapper memberMapper;

	public MemberServiceImpl(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

    @Override
    public int idCheck(String userId) throws Exception {
        return memberMapper.idCheck(userId);
    }

    @Override
    public int joinMember(MemberDto memberDto) throws Exception {
        return  memberMapper.joinMember(memberDto);
    }
    
	@Override
	public AuthenticationResponse loginMember(AuthenticationRequest authenticationRequest) throws Exception {
		return memberMapper.loginMember(authenticationRequest);
	}

    @Override
    public int modifyMember(MemberDto memberDto) throws Exception {
        return memberMapper.modifyMember(memberDto);
    }

    @Override
    public int deleteMember(String userId) throws Exception {
        return memberMapper.deleteMember(userId);
    }

    @Override
    public String searchId(MemberDto memberDto) throws Exception {
        return memberMapper.searchId(memberDto);
    }

    @Override
    public String searchPwd(MemberDto memberDto) throws Exception {
        return memberMapper.searchPwd(memberDto);
    }
//	JWT
	@Override
	public void saveRefreshToken(String userId, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("refreshToken", refreshToken);
		memberMapper.saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userId) throws Exception {
		return memberMapper.getRefreshToken(userId);
	}

	@Override
	public void deleRefreshToken(String userId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("refreshToken", null);
		memberMapper.deleteRefreshToken(map);
	}

	@Override
	public Optional<MemberDto> getMember(String userId) throws Exception {
		return memberMapper.getMember(userId);
	}

}
