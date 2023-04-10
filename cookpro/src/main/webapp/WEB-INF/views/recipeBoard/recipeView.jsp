<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	request.setCharacterEncoding("utf-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="recipe" value="${recipeMap.recipe }" />
<c:set var="imageFileList" value="${recipeMap.imageFileList }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>레시피</title>
</head>
<body>
	<h1>글보기</h1>
<style>
	#tr_btn_modify{
		display:none;
	}
</style>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	function backToList(obj){
		obj.action="${contextPath}/recipeboard/recipeList.di";
		obj.submit();
	}
	
	function fn_enable(obj){
		document.getElementById("rp_title").disabled=false;
		document.getElementById("rp_detail").disabled=false;
	/*	imageFileName = document.getElementById("rp_imageFileName");
		if(imageFileName != null && imageFileName.length != 0){
			document.getElementById("rp_imageFileName").disabled=false;
		}*/
		document.getElementById("tr_btn_modify").style.display="block";
		document.getElementById("tr_btn").style.display="none";
	}
	
	function fn_modify_recipe(obj){
		obj.action ="${contextPath}/recipeboard/modRecipe.do";
		obj.method = "post";
		obj.submit();
	}
	
	function fn_remove_recipe(url, recipe_no){
		let form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", url);
		
		let recipe_noInput = document.createElement("input");
		articleNoInput.setAttribute("type", "hidden");
		articleNoInput.setAttribute("name", "recipe_no");
		articleNoInput.setAttribute("value", recipe_no);
		
		form.appendChild(articleNoInput);
		document.body.appendChild(form);
		form.submit();
	}
	
	function readURL(input){
		if(input.files && input.files[0]) {
			let reader = new FileReader();
			reader.onload = function(e) {
				$("#preview").attr("src", e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	
</script>
</body>
	<form name="frmRecipe" method="post" enctype="multipart/form-data">
		<table border="0" align="center">
			<tr>
				<td width="150" align="center" bgcolor="#D3D3D3">글번호</td>
				<td>
					<input type="text" value="${recipe.recipe_no }" disabled="disabled">
					<input type="hidden" value="${recipe.recipe_no }" name="recipe_no">
				</td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#D3D3D3">작성자</td>
				<td>
					<input type="text" value="${recipe.id }" name="id" disabled="disabled">
				</td>
			</tr>
			<tr>
				<td width="20%" align="center" bgcolor="#FF9933">작성일</td>
				<td>
					<input type="text" value="<fmt:formatDate value='${recipe.writeDate}'/>" disabled="disabled">
				</td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#D3D3D3">제목</td>
				<td>
					<input type="text" value="${recipe.recipe_title }" name="recipe_title" id="rp_title" disabled="disabled">
				</td>				
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">내용</td>
				<td>
					<textarea rows="20" cols="60" name="content" id="rp_content" disabled="disabled">
						${recipe.recipe_detail}</textarea>
				</td>
			</tr>
			<tr id="tr_btn_modify">
				<td colspan="2" align="center">
				<c:if test="${member.id == recipe.id }">
					<input type='button' value="수정반영하기" onclick="fn_modify_recipe(frmRecipe)">
					<input type='button' value="삭제하기" onclick="fn_remove_recipe('${contextPath}/board/removeRecipe.do',${recipe.recipe_no })">
				</c:if>
					<input type="button" value="목록" onclick="backToList(this.form)">
				</td>
		</table>
	</form>
</html>