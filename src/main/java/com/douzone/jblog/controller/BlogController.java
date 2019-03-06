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
import org.springframework.web.servlet.ModelAndView;

import com.douzone.dto.JSONResult;
import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.service.FileUploadService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
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

	@RequestMapping({ "", "/{category}","/{category}/{post}" })
	public String mappingParameter(
			@PathVariable("id") String id, // 접속할 블로그의 id
			@PathVariable Optional<Long> category,
			@PathVariable Optional<Long> post,
			Model model) {
		
		// post비교
		if(post.isPresent()) {
			//postDao.
		} else {
			// default setting -> post desc 1번 글
			
		}
		
		// category 비교
		if(category.isPresent()) {
			//cateGoryDao.
		} else {
			// default setting -> 미분류 카데고리
		}
		
		if (!id.isEmpty()) { // id가 있다면..
			
			return urlMapped(id, "blog/blog-main", model);
		}
		
		// 아무것도 없을경우 error
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
		
		return urlMapped(userId, "blog/blog-main", model);
	}
	
	public String urlMapped(String id, String viewName, Model model) { // parameter는 접속할 블로그 user의 id
		BlogVo vo = blogService.getBlogInfo(id);
		System.out.println(vo);
		
		model.addAttribute("blogUserId", id);
		model.addAttribute("blogVo", vo);
		
		return viewName;
	}

}
