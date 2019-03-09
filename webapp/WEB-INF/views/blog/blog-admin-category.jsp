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

	var count = 0;
	var render = function(vo, mode) {
		count++;
		var htmls = "<tr data-no='"+vo.no+"'>" +
						"<td>" + count + "</td>" +
						"<td>" + vo.name + "</td>" + 
						"<td>" + vo.comment + "</td>" +
						"<td>" + vo.postCount + "</td>" +
						"<td><a href='' data-no='"+vo.no+"'><img src='${pageContext.request.contextPath}/assets/images/delete.jpg'></a></td>"
					"</tr>";
		if (mode == true) { //앞
			$(".admin-cat").prepend(htmls);
		} else { // 뒤
			$(".admin-cat").append(htmls);
		}
	}
	// list
	$.ajax({
		url:"${pageContext.servletContext.contextPath }/${authUser.id }/admin/category/list",
		type: "post",
		dataType: "json",
		data: "",
		success: function(response) {
			if (response.result == "fail") {
				console.warn(response.message);
				return;
			}
			console.log(response.data);
			
//			count = 0;
			
			// rendering
			$.each(response.data, function(index, vo) {
				render(vo, false);
			});
		},
		error: function(xhr, status, e) {
			console.error(status + " : " + e);
		}
	});

	$(function() {
		// category insert
		$("#btn-insert").click(function() {
			$.ajax({
				url:"${pageContext.servletContext.contextPath }/${authUser.id }/admin/category/insert",
				type: "post",
				dataType: "json",
				data: "name="+ $("#name").val() + "&comment=" + $("#desc").val(),
				success: function(response) {
					if (response.result == "fail") {
						console.warn(response.data);
						return;
					}
					render(response.data, false);
				},
				error: function(xhr, status, e) {
					console.error(status + " : " + e);
				}
			});
		});

		// category delete
		$(document).on("click", ".admin-cat tr td a", function() {
			var no = $(this).data("no");
			
			// delete 실행 시 post의 갯수가 1개 이상이면 
			// 반드시 x버튼 비활성화 및 삭제 수행 못함
			$.ajax({
				url:"${pageContext.servletContext.contextPath }/${authUser.id }/admin/category/delete",
				type: "post",
				dataType: "json",
				data: "no=" + no,
				success: function(response) {
					if(response.data == true) {
						// 삭제 성공
						console.log("삭제 성공");
						$(".admin-cat tr[data-no="+ no +"]").remove();
					} else {
						// 삭제 실패
						alert('카테고리를 삭제할 수 없습니다.');
					}
				},
				error: function(xhr, status, e) {
					console.error(status + " : " + e);
				}
			});
		});
	});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/include/blog-header.jsp"/>
		
		<div id="wrapper">
			<div id="content" class="full-screen">
				<c:import url="/WEB-INF/views/include/blog-mid-header.jsp" >
					<c:param name="menu" value="category"/>
				</c:import>
		      	<table class="admin-cat">
		      		<tr>
		      			<th>번호</th>
		      			<th>카테고리명</th>
		      			<th>설명</th>
		      			<th>포스트 수</th>
		      			<th>삭제</th>      			
		      		</tr>		  
				</table>
      	
      			<h4 class="n-c">새로운 카테고리 추가</h4>
		      	<table id="admin-cat-add">
		      		<tr>
		      			<td class="t">카테고리명</td>
		      			<td><input type="text" name="name" id="name"></td>
		      		</tr>
		      		<tr>
		      			<td class="t">설명</td>
		      			<td><input type="text" name="desc" id="desc"></td>
		      		</tr>
		      		<tr>
		      			<td class="s">&nbsp;</td>
		      			<td><button id="btn-insert">카테고리 추가</button></td>
		      		</tr>      		      		
		      	</table> 
			</div>
		</div>
		<c:import url="/WEB-INF/views/include/blog-footer.jsp"/>
	</div>
</body>
</html>