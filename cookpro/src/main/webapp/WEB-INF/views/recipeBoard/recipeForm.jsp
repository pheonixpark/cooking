<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setCharacterEncoding("utf-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
function readURL(input) {
	if(input.files && input.files[0]){
		let reader = new FileReader();
		reader.onload = function(e) {
			$('#preview').attr('src', e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
}

function backToList(obj){
	obj.action = "${contextPath}/board/listArticles.do";
	obj.submit();
}
let cnt = 1;
function fn_addFile(){
	$("#d_file").append("<br>" + "<input type='file' name='file"+cnt+"'>" + 
			"<input type='button' value='사진 삽입' onclick='fn_insertFile()'></div>");
	cnt++;
}
function fn_insertFile(){
	
}
</script>
<title>Insert title here</title>
</head>
<body>
	<form name="recipeForm" action="post" action="${contextPath }/recipeboard/addNewRecipe.do" enctype="multipart/form-data">
		<table>
			<tbody>
				<tr>
					<td>
						<select name="category" class="category" data-filter-message-isnull="탭을 선택하시기 바랍니다">
							<option>한식</option>
							<option>중식</option>
							<option>일식</option>
							<option>양식</option>
							<option>기타</option>
						</select>
					</td>
					<td>
						<input type="text" name="recipe_title" maxlength="50">
					</td>
				</tr>
				<tr>
					<td>
					 	<textarea name="recipe_detail" rows="20" cols="60" maxlength="5000"></textarea>
					</td>
				</tr>
			</tbody>
			<tr>
				<td>
					<input type="button" value="사진 추가" onclick="fn_addFile()"> 
				</td>
			</tr>
			<tr>
				<td colspan="4"><div id="d_file"></div></td>
				
			</tr>
			<tr>
				<td align="right"></td>
				<td colspan="2">
					<input type="submit" value="글쓰기">
					<input type="button" value="목록보기" onclick="backToList(this.form)">
				</td>
			</tr>
		</table>

	</form>
</body>
</html>