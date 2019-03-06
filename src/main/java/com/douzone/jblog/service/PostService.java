package com.douzone.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.PostDao;
import com.douzone.jblog.vo.PostVo;

@Service
public class PostService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private CategoryService categoryService;
	
	public void insert(PostVo postVo, String categoryName, String userId) {
		long categoryNo = categoryService.getNo(categoryName, userId);
		
		postVo.setCategoryNo(categoryNo);
		
		postDao.insert(postVo);
	}
}
