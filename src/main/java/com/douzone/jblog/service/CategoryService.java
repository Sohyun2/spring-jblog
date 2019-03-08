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
	@Autowired
	private UserService userService;
	
	public List<CategoryVo> getList(String userId) {
		long userNo = userDao.getNo(userId);
		
		return categoryDao.getList(userNo);
	}

	public CategoryVo insert(String userId, CategoryVo categoryVo) {
		long userNo = userDao.getNo(userId);
		categoryVo.setUserNo(userNo);
		categoryVo.setPostCount(0L);
		
		categoryDao.insert(categoryVo);
		return categoryVo;
	}

	public boolean delete(Long categoryNo) {
		long countPost = categoryDao.countPost(categoryNo);
		
		if(countPost >= 1 ) {
			// post 갯수가 1 이상이므로 category를 지울 수 없음
			return false;
		}
		
		categoryDao.delete(categoryNo);
		
		return true;
	}

	public long getNo(String categoryName, String userId) {
		long userNo = userService.getNo(userId);
		
		CategoryVo categoryVo = new CategoryVo();
		categoryVo.setName(categoryName);
		categoryVo.setUserNo(userNo);
		
		return categoryDao.getNo(categoryVo);
		
	}

	public long getLastCategoryNo(String userId) {
		long userNo = userService.getNo(userId);
		return categoryDao.getLastCategoryNo(userNo);
	}

}
