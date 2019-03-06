package com.douzone.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.BlogVo;

@Repository
public class BlogDao {

	@Autowired
	private SqlSession sqlSession;

	public void createBlog(long userNo) {
		sqlSession.insert("blog.createBlog", userNo);
	}
	
	public BlogVo getBlogInfo(Long userNo) {
		return sqlSession.selectOne("blog.getBlogInfo", userNo);
	}

	public void update(BlogVo vo) {
		sqlSession.update("blog.update", vo);
	}

}
