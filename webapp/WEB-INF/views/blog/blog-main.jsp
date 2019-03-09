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
<script type="text/javascript">
	$(function() {
		// comment insert
		$("#btn-insert").click(function() {
			$.ajax({
				url:"${pageContext.servletContext.contextPath }/${authUser.id }/comment/insert",
				type: "post",
				dataType: "json",
				data: "content="+ $("#comment-conetent").val() + "postNo="+"${post.no}",
				success: function(response) {
					if (response.result == "fail") {
						console.warn(response.data);
						return;
					}
					//render(response.data, false);
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
			<div id="content">
	
				<!-- 최근 게시글 보여주기 -->
				<div class="blog-content">
					<c:choose>
						<c:when test="${empty post }">
							<p>등록된 게시글이 없습니다.</p>
						</c:when>
						<c:otherwise>
							<h4>${post.title }</h4>
							<p>
								${post.content }
							<p>
						</c:otherwise>
					</c:choose>
					
				</div>
				
				<!-- post comment -->
			
				<table class="tbl-ex">
					<tr>
						<td><input type="text" name="comment-conetent"/></td>
						<td><button id="btn-insert">댓글 등록</button></td>
					</tr>	
					<c:forEach items="${commentList }" var="c_vo">
						<tr>
							<td>
								<img src="${pageContext.request.contextPath}/assets/images/reply.png" style="width:13px; padding-right:5px;">
									${c_vo.content }
							</td>
						</tr>
					</c:forEach>
				</table>
				<!-- 카테고리 post list보여주기 -->
				<ul class="blog-list">
					<c:forEach items="${postList }" var="vo">
						<li><a href="${pageContext.request.contextPath}/${blogUserId }/${vo.categoryNo }/${vo.no }">${vo.title }</a> <span>${vo.regDate }</span>	</li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img onerror="this.src='${pageContext.servletContext.contextPath }/assets/images/default-image.jpg'"
					 src="${pageContext.request.contextPath}/assets${blogVo.logo }">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			<ul>
				<c:forEach items="${categoryList }" var="vo">
					<li><a href="${pageContext.request.contextPath}/${blogUserId }/${vo.no }">${vo.name }</a></li>
				</c:forEach>
			</ul>
		</div>
		
		<c:import url="/WEB-INF/views/include/blog-footer.jsp"/>
	</div>
</body>
</html>