package com.douzone.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.PostVo;

@Repository
public class PostDao {

	@Autowired
	private SqlSession sqlSession;

	public void insert(PostVo postVo) {
		sqlSession.insert("post.insert", postVo);
	}
	
	public PostVo getLastPost(Long lastCategoryNo) {
		return sqlSession.selectOne("post.getLastPost", lastCategoryNo);
	}

	public List<PostVo> getList(long categoryNo) {
		return sqlSession.selectList("post.getList", categoryNo);
	}

	public PostVo getPost(long postNo) {
		return sqlSession.selectOne("post.getPost", postNo);
	}
}
