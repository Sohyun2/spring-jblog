package com.douzone.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;

@Repository
public class CategoryDao {
	
	@Autowired
	private SqlSession sqlSession;
	public void defaultCategory(long userNo) {
		sqlSession.insert("category.defaultCategory", userNo);
	}
	public List<CategoryVo> getList(long userNo) {
		return sqlSession.selectList("category.getList", userNo);
	}
	public void insert(CategoryVo categoryVo) {
		sqlSession.insert("category.insert", categoryVo);
	}
	public Long countPost(Long categoryNo) {
		return sqlSession.selectOne("category.countPost", categoryNo);
	}
	public void delete(Long categoryNo) {
		sqlSession.delete("category.delete", categoryNo);
	}
	public long getNo(CategoryVo categoryVo) {
		return sqlSession.selectOne("category.getNo", categoryVo);
	}
}
