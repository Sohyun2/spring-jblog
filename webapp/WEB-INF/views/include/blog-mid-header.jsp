<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

	<ul class="admin-menu">
		<c:choose>
			<c:when test='${param.menu == "write" }'>
				<li>기본설정</li>
				<li><a href="${pageContext.request.contextPath}/${authUser.id }/admin/category">카테고리</a></li>
				<li class="selected"><a href="${pageContext.request.contextPath}/${authUser.id }/admin/write">글작성</a></li>
			</c:when>
			<c:when test='${param.menu == "category" }'>
				<li>기본설정</li>
				<li class="selected"><a href="${pageContext.request.contextPath}/${authUser.id }/admin/category">카테고리</a></li>
				<li><a href="${pageContext.request.contextPath}/${authUser.id }/admin/write">글작성</a></li>
			</c:when>
			<c:otherwise>
				<li class="selected">기본설정</li>
				<li><a href="${pageContext.request.contextPath}/${authUser.id }/admin/category">카테고리</a></li>
				<li><a href="${pageContext.request.contextPath}/${authUser.id }/admin/write">글작성</a></li>
			</c:otherwise>
		</c:choose>
	</ul>