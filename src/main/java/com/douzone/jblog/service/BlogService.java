package com.douzone.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.BlogDao;
import com.douzone.jblog.repository.UserDao;
import com.douzone.jblog.vo.BlogVo;

@Service
public class BlogService {
	
	@Autowired
	private BlogDao blogDao;
	@Autowired
	private UserDao userDao;

	// blog main페이지 title, logo 가져오기
	public BlogVo getBlogInfo(String userId) {
		// id를 이용해 user의 no 가져오기
		Long userNo = userDao.getNo(userId);
		
		return blogDao.getBlogInfo(userNo);
	}

	public void update(String userId, BlogVo vo) {
		// id를 이용해 user의 no 가져오기
		Long userNo = userDao.getNo(userId);
		vo.setUserNo(userNo);
		
		blogDao.update(vo);
	}
}
