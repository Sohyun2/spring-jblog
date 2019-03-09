package com.douzone.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.BlogDao;
import com.douzone.jblog.repository.CategoryDao;
import com.douzone.jblog.repository.UserDao;
import com.douzone.jblog.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private BlogDao blogDao;
	@Autowired
	private CategoryDao categoryDao;
	
	public void join(UserVo userVo) {
		userDao.join(userVo);
		long userNo = userVo.getNo();
		
		// 회원 등록 동시에 blog 생성
		blogDao.createBlog(userNo);
		// 블로그 생성 동시에 category 미분류 생성
		categoryDao.defaultCategory(userNo);
	}

	public UserVo login(String id, String password) {
		UserVo userVo = new UserVo();
		userVo.setId(id);
		userVo.setPassword(password);
		
		return userDao.login(userVo);
	}

	public long getNo(String userId) {
		return userDao.getNo(userId);
	}

	public boolean existEmail(String id) {
		UserVo userVo = userDao.get(id);
		return userVo != null;
	}
}
