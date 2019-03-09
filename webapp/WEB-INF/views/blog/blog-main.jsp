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
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/include/blog-header.jsp"/>
		<div id="wrapper">
			<div id="content">
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