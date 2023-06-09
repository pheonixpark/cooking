<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리스트</title>
<script>
	function fn_recipeForm(isLogOn, recipeForm, loginForm) {
		if(isLogOn != '' && isLogOn != 'false') {
			location.href=recipeForm;
		} else {
			alert('로그인 후 글쓰기가 가능합니다.');
			location.href=loginForm+'?action=/recipeboard/recipeForm.do';
		}
	}
	

</script>
</head>
<body>
	<table class="main-table">
		<h1 style="text-align:center; color:#FF7629;">중식</h1>
		<tr>
			<td colspan="4" align="left" style="padding: 5px;">
				<div>
			<a class="no-underline-tab" href="${contextPath }/recipeboard/recipeListKr.do">&nbsp; 한식 &nbsp;</a>
			<a class="no-underline-tab" href="${contextPath }/recipeboard/recipeListCn.do">&nbsp; 중식 &nbsp;</a>
			<a class="no-underline-tab" href="${contextPath }/recipeboard/recipeListJp.do">&nbsp; 일식 &nbsp;</a>
			<a class="no-underline-tab" href="${contextPath }/recipeboard/recipeListEn.do">&nbsp; 양식 &nbsp;</a>
			<a class="no-underline-tab" href="${contextPath }/recipeboard/recipeListOt.do">&nbsp; 기타 &nbsp;</a>
			<br>
				</div>
			<td>
		</tr>
		<tr class="main-table" style="height:12px; color: white; font-size:18px; background: #FF7629;">
			<td>글번호</td>
			<td>작성자</td>
			<td>탭</td>
			<td>제목</td>
			<td>작성일자</td>
			<td>조회수</td>
			<td>추천수</td>
		</tr>
		<c:choose>
			<c:when test="${recipesList == null }">
				<tr height=10">
					<td colspan="4">
						<b><span style="font-size:9pt;">등록된 글이 없습니다</span></b>
					</td>
				</tr>
			</c:when>
			<c:when test="${recipesList != null }">
				<c:forEach var="recipe" items="${recipesList }" varStatus="recipe_no">
					<tr align="center">
						<td width="5%">${recipe.recipe_no }</td>
						<td width="5%">${recipe.id }</td>
						<td width="5%">${recipe.recipe_tab }</td>
						<td align="left" width="50%">
							<span style="padding-right:30px;"></span>
							<a class="no-underline" href="${contextPath }/recipeboard/recipeView.do?recipe_no=${recipe.recipe_no}" >
								${recipe.recipe_title }
							</a>
						</td>
						<td width="10%"><fmt:formatDate value="${recipe.writeDate }"/></td>
						<td width="5%">${recipe.recipe_views }</td>
						<td width="5%">${recipe.recipe_like }</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	<div class="cls2">
		<c:if test="totRecipes != null">
			<c:choose>
				<c:when test="${totRecipes > 100 }">
					<c:forEach var="page" begin="1" end="10" step="1">
						<c:if test="${section > 1 && page==1 }">
							<a class="no-uline" href="${contextPath }/recipeboard/recipeList.do?section=${section}&pageNum=${(section-1)*10+1}">&nbsp;</a>						
						</c:if>
						<a class="no-uline" href="${contextPath }/recipeboard/recipeList.do?section=${section}&pageNum=${page }">${(section-1)*10 + page }</a>
						<c:if test="${page == 10 }">
							<a class="no-uline" href="${contextPath }/recipeboard/recipeList.do?section=${section}&pageNum=${(section-1)*10+1}">&nbsp;next</a>
						</c:if>
					</c:forEach>
				</c:when>
				<c:when test="${totRecipes == 100 }">
					<c:forEach	var="page" begin="1" end="10" step="1">
						<a class="no-uline" href="#">{page}</a>
					</c:forEach>
				</c:when>
				<c:when test="${totRecipes < 100 }">
				<c:forEach var="page" begin="1" end="${totRecipes/10 +1 }" step="1">
					<c:choose>
						<c:when test="${page == pageNum }">
							<a class="sel-page" href="${contextPath }/recipeboard/recipeList.do?section=${section }&pageNum=${page }">${page }</a> 
						</c:when>
						<c:otherwise>
							<a class="no-uline" href="${contextPath }/recipeboard/recipeList.do?section=${section }&pageNum=${page }">${page }</a> 
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
	</div>
	<br>
	<a class="no-underline-orange" href="javascript:fn_recipeForm('${isLogOn }','${contextPath }/recipeboard/recipeForm.do','${contextPath }/member/loginForm.do')">
	<b style="font-size:20px;">글쓰기</b></a>
</body>
</html>