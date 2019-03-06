<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
	var render = function(vo, mode) {
		var htmls = "<tr>" +
						"<td>" + vo.no + "</td>" +
						"<td>" + vo.name + "</td>" + 
						"<td>" + vo.comment + "</td>" +
						"<td>" + vo.postCount + "</td>"
						"<td><a href=''>" +
							"<img src='${pageContext.request.contextPath}/assets/images/delete.jpg'>"+
						"</td>"
					"</tr>";
				
		if (mode == true) { //앞
			$(".admin-cat").prepend(htmls);
		} else { // 뒤
			$(".admin-cat").append(htmls);
		}
	}
	$.ajax({
		url:"${pageContext.servletContext.contextPath }/${authUser.id }/admin/category/list",
		type: "get",
		dataType: "json",
		data: "",
		success: function(response) {
			if (response.result == "fail") {
				console.warn(response.message);
				return;
			}
			console.log(response.data);

			// rendering
			$.each(response.data, function(index, vo) {
				render(vo, false);
			});
		},
		error: function(xhr, status, e) {
			console.error(status + " : " + e);
		}
	});
	
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/include/blog-header.jsp"/>
		
		<div id="wrapper">
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a href="${pageContext.request.contextPath}/${authUser.id }/admin/basic">기본설정</a></li>
					<li class="selected">카테고리</li>
					<li><a href="">글작성</a></li>
				</ul>
		      	<table class="admin-cat">
		      		<tr>
		      			<th>번호</th>
		      			<th>카테고리명</th>
		      			<th>포스트 수</th>
		      			<th>설명</th>
		      			<th>삭제</th>      			
		      		</tr>		  
				</table>
      	
      			<h4 class="n-c">새로운 카테고리 추가</h4>
		      	<table id="admin-cat-add">
		      		<tr>
		      			<td class="t">카테고리명</td>
		      			<td><input type="text" name="name"></td>
		      		</tr>
		      		<tr>
		      			<td class="t">설명</td>
		      			<td><input type="text" name="desc"></td>
		      		</tr>
		      		<tr>
		      			<td class="s">&nbsp;</td>
		      			<td><input type="submit" value="카테고리 추가"></td>
		      		</tr>      		      		
		      	</table> 
			</div>
		</div>
		<div id="footer">
			<p>
				<strong>Spring 이야기</strong> is powered by JBlog (c)2016
			</p>
		</div>
	</div>
</body>
</html>