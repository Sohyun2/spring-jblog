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
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script type="text/javascript">
	$(function() {
		$("#join-form").submit(function() {
			//1. 이름 체크
			if($("#name").val() == ""){
				alert("이름은 필수 입력 항목입니다.");
				$("#name").focus();
				return false;
			}
			
			//2-1. 아이디 비어 있는 지 확인
			if($("#blog-id").val() == ""){
				alert("id는 필수 입력 항목입니다.");
				$("#blog-id").focus();
				return false;
			}
			
			//2-2. 아이디 중복체크 유무
			// 중복 체크를 할 경우 체크이미지가 보인다.
			// 이미지가 보이지 않을 경우 중복체크를 하지 않은 경우이므로..
			if($("#img-checkemail").is(":visible") == false) {
				alert("id 중복 체크를 해야합니다.");
				$("#blog-id").focus();
				return false;
			}
			
			//3. 비밀번호 확인
			if($("input[type='password']").val() == ""){
				alert("비밀번호는 필수 입력 항목입니다.");
				$("input[type='password']").focus();
				return false;
			}
			
			//4. 약관동의
			if($("#agree-prov").is(":checked") == false){
				alert("약관 동의를 해야 합니다.");
				return false;
			}
			
			return true;
		});

		$("#blog-id").change(function() {
			$("#btn-checkemail").show();
			$("#img-checkemail").hide();
		});
		
		$("#btn-checkemail").click(function(){
			var id = $("#blog-id").val();
			if(id == ""){
				alert("id를 입력해주세요")
				return;
			}
			
			$.ajax({
				url: "${pageContext.servletContext.contextPath }/user/api/checkemail",
				type: "post",
				dataType: "json",
				data: "id=" + id,
				success: function(response){
					if(response.data == true){
						alert("이미 존재하는 이메일입니다. 다른 이메일을 사용해 주세요.");
						$("#blog-id").val("").focus();
						return;
					}
					
					// 사용가능한 이메일
					$("#btn-checkemail").hide();
					$("#img-checkemail").show();
				},
				error: function(xhr, status, e){
					console.error(status + ":" + e);
				}
			});
		});
	});
</script>
</head>
<body>
	<div class="center-content">
		<c:import url="/WEB-INF/views/include/header.jsp"/>
		
		<form class="join-form" id="join-form" method="post" action="${pageContext.request.contextPath}/user/join">
			<label class="block-label" for="name">이름</label>
			<input id="name" name="name" type="text" value="">
			
			<label class="block-label" for="blog-id">아이디</label>
			<input id="blog-id" name="id" type="text"> 
			<input id="btn-checkemail" type="button" value="id 중복체크">
			<img id="img-checkemail" style="display: none;" src="${pageContext.request.contextPath}/assets/images/check.png">

			<label class="block-label" for="password">패스워드</label>
			<input id="password" name="password" type="password" />

			<fieldset>
				<legend>약관동의</legend>
				<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
				<label class="l-float">서비스 약관에 동의합니다.</label>
			</fieldset>

			<input type="submit" value="가입하기">

		</form>
	</div>
</body>
</html>
