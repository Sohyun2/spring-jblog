package com.douzone.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.UserVo;

@Repository
public class UserDao {
	
	@Autowired
	private SqlSession sqlSession;

	public void join(UserVo userVo) {
		sqlSession.insert("user.insert", userVo);
	}

	public UserVo login(UserVo userVo) {
		return sqlSession.selectOne("user.getByIdAndPassword", userVo);
	}

	public Long getNo(String userId) {
		return sqlSession.selectOne("user.getNoById", userId);
	}
	
	// 회원가입 시 이메일 존재유무 확인 method
	public UserVo get(String id) {
		return sqlSession.selectOne("user.getByEmail", id);
	}
}
