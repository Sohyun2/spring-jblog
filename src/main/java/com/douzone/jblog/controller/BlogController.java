package com.douzone.jblog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.dto.JSONResult;
import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.service.CommentService;
import com.douzone.jblog.service.FileUploadService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.CommentVo;
import com.douzone.jblog.vo.PostVo;

@Controller
@RequestMapping("/{id:(?!assets).*}") // 정규표현식 기입해준거임..
public class BlogController {
	// 해당 class method에는 반드시 @PathVariable String id가 필요하다
	// 안적어주면 bad Request..

	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private BlogService blogService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;

	@RequestMapping({ "", "/{category}","/{category}/{post}" })
	public String mappingParameter(
			@PathVariable("id") String id, // 접속할 블로그의 id
			@PathVariable Optional<Long> category,
			@PathVariable Optional<Long> post,
			Model model) {
		
		long categoryNo = 0;		

		// category 비교
		if(category.isPresent()) {
			categoryNo = category.get(); // url로 넘어온 categoryNo 받아오기			
		} else {
			// 제일 최근에 만들어진 카테고리 no가져오기
			categoryNo = categoryService.getLastCategoryNo(id);
			System.out.println("마지막 카테고리 no>> " + categoryNo);
		}
		
		//PostVo postVo = null;		
		// post비교
		if(post.isPresent()) {
			long postNo = post.get();
			
			PostVo postVo = postService.getPost(postNo);
			model.addAttribute("post", postVo);
			System.out.println("post가져오기>>" + postVo);
			
			// 해당 post의 comment 가져오기
			List<CommentVo> commentList = commentService.get(postNo);
			model.addAttribute("commentList", commentList);
			System.out.println("commentList....." + commentList);
		} else {
			// 메인 글을 뿌리기 위해서
			// 제일 최근에 만들어진 카테고리의 제일 최근에 적힌 post 가져오기
			PostVo postVo  = postService.getLastPost(categoryNo); // 마지막 글 가져오기
			model.addAttribute("post", postVo);
			System.out.println("카테고리 최근 추가 글 >> " + postVo);

			// 해당 post의 comment 가져오기
			List<CommentVo> commentList = commentService.get(postVo.getNo());
			model.addAttribute("commentList", commentList);
			System.out.println("commentList....." + commentList);
		}
		
		if (!id.isEmpty()) { // id가 있다면..
			// 카테고리 리스트 뽑기 // 공통적으로 있어야할 부분
			List<CategoryVo> categoryList = categoryService.getList(id);
			model.addAttribute("categoryList", categoryList);

			// 하단 post 리스트 뿌리기 위해서
			// 제일 최근에 만들어진 카테고리에 대한 post 리스트 뽑아오기
			List<PostVo> postList = postService.getList(categoryNo);
			model.addAttribute("postList", postList);
			System.out.println("카테고리 post list>> " + postList);	
			
			return urlMapped(id, "blog/blog-main", model);
		}
		
		// 아무것도 없을경우 jblog-main으로 감..
		return "";
	}

	@RequestMapping("/admin/basic")
	public String adminBasic(@PathVariable("id") String id, Model model) {
		return urlMapped(id, "blog/blog-admin-basic", model); //현재 접속된 아이디

	}
	
	@RequestMapping("/admin/update")
	public String adminUpdate(
			@PathVariable("id") String id,
			@ModelAttribute BlogVo vo,
			@RequestParam("logo-file") MultipartFile multipartFile,
			Model model) {
		System.out.println("in BlogController/update");
		
		String imageFile = fileUploadService.restore(multipartFile);
		vo.setLogo(imageFile);
		blogService.update(id, vo);

		return urlMapped(id, "redirect:/"+id, model); //현재 접속된 아이디
	}
	
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id, Model model) {
		System.out.println("/admin/category..GET");
		
		return urlMapped(id, "/blog/blog-admin-category", model);
	}
	
	@ResponseBody
	@RequestMapping("/admin/category/list")
	public JSONResult categoryList(@PathVariable("id") String id) {
		System.out.println("/admin/category..POST");
		
		// authUser id가 가지고 있는 categoryList를 가지고 가기
		List<CategoryVo> list = categoryService.getList(id);
		System.out.println(list);
		
		//JSON객체에 담아서 return
		if(list == null) {
			//만약 list를 뽑아오지 못했을 경우...
			return JSONResult.fail("");
		}
		
		return JSONResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/admin/category/insert", method=RequestMethod.POST)
	public JSONResult categoryInsert(@PathVariable("id") String id,
			@RequestParam("name") String name,
			@RequestParam("comment") String comment) {
		System.out.println("/admin/category/insert..POST");
		
		CategoryVo categoryVo = new CategoryVo();
		categoryVo.setName(name);
		categoryVo.setComment(comment);
		
		categoryVo = categoryService.insert(id, categoryVo);
		System.out.println(categoryVo);
		
		return JSONResult.success(categoryVo);
	}
	
	@ResponseBody
	@RequestMapping(value="/admin/category/delete", method=RequestMethod.POST)
	public JSONResult categoryDelete(@PathVariable("id") String id, @RequestParam("no") Long categoryNo) {
		System.out.println("/admin/category/delete..POST");
		System.out.println(categoryNo);
		// post 갯수 확인..
		boolean data = categoryService.delete(categoryNo);
		
		return JSONResult.success(data);
	}
	
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("id") String userId, Model model) {
		System.out.println("/admin/write..GET");
		
		//category
		List<CategoryVo> list = categoryService.getList(userId);
		model.addAttribute("categoryList", list);
		
		
		return urlMapped(userId, "/blog/blog-admin-write", model);
	}

	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(@PathVariable("id") String userId,
			@ModelAttribute PostVo postVo,
			@RequestParam("category") String category,
			Model model) {
		System.out.println("/admin/write..POST");
				
		postService.insert(postVo, category, userId);
		
		return urlMapped(userId, "redirect:/"+userId, model);
	}
	
//	@RequestMapping("/comment/insert")
//	@ResponseBody
//	public JSONResult commentInsert(@RequestParam("content")String content, @RequestParam("postNo")Long postNo) {
//		System.out.println("content: " + content);
//		System.out.println("postNo: " + postNo);
//		return null;
////		return JSONResult.success(data);
//	}
//	
	public String urlMapped(String id, String viewName, Model model) { // parameter는 접속할 블로그 user의 id
		BlogVo vo = blogService.getBlogInfo(id);
		System.out.println(vo);
		
		model.addAttribute("blogUserId", id);
		model.addAttribute("blogVo", vo);
		
		return viewName;
	}

}
