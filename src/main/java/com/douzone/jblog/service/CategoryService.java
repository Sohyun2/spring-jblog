package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.CategoryDao;
import com.douzone.jblog.repository.UserDao;
import com.douzone.jblog.vo.CategoryVo;

@Service
public class CategoryService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private CategoryDao categoryDao;
	
	public List<CategoryVo> getList(String userId) {
		long userNo = userDao.getNo(userId);
		
		return categoryDao.getList(userNo);
	}

}
