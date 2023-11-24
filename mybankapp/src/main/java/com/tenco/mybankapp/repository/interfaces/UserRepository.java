package com.tenco.mybankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.mybankapp.repository.entity.User;

@Mapper
public interface UserRepository {
	
	
	public void insert(User user);//사용자등록
	public void update(User user);//사용자수정
	public void delete(Integer id);//사용자삭제
	public User findById(Integer id);//사용자 조회
	public List<User> findAll();//사용자 전체 조회
	
}
