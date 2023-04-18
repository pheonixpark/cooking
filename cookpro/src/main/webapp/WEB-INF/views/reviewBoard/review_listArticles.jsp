<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
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
<title>글목록창</title>
<script>
	function fn_articleForm(isLogOn, articleForm, loginForm) {
		if(isLogOn != '' && isLogOn != 'false') {
			location.href=articleForm;
		} else {
			alert('로그인 후 글쓰기가 가능합니다.');
			location.href=loginForm+'?action=/reviewBoard/reveiw_Form.do';
		}
	}
</script>
</head>
<body>
	<table align="center" border="1" width="100%">
		<tr height="10" align="center" bgcolor="lightgreen">
			<td>글번호</td>
			<td>작성자</td>
			<td>제목</td>
			<td>작성일자</td>
		</tr>
		<c:choose>
			<c:when test="${articlesList == null }">
				<tr height="10">
					<td colspan="4">
						<b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
					</td>
				</tr>
			</c:when>
			<c:when test="${articlesList != null }">
				<c:forEach var="article" items="${articlesList }" varStatus="articleNum">
					<tr align="center">
						<td width="5%">${articleNum.count }</td>
						<td width="10%">${reviewboard.id }</td>
						<td align="left" width="35%">
							<span style="padding-right:30px;"></span>
							<c:choose>
								<c:when test="${reviewboard.review_level > 1 }">
									<c:forEach begin="1" end="${reviewboard.review_level }" step="1">
										<span style="padding-left:20px;"></span>
									</c:forEach>
									<span style="font-size:12px;">[답변]</span>
									<a class="cls1" href="${contextPath }/reviewBoard/review_viewArticle.do?articleNo=${reviewboard.review_no}">
										${reviewboard.review_title }
									</a>
								</c:when>
								<c:otherwise>
									<a class="clas1" href="${contextPath }/reviewBoard/review_viewArticle.do?articleNo=${reviewboard.review_no}">
										${reviewboard.title }
									</a>
								</c:otherwise>
							</c:choose>
						</td>
						<td width="10%"><fmt:formatDate value="${reviewboard.writeDate }" /></td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	<div class="cls2">
		<c:if test="${totArticles != null }">
			<c:choose>
				<c:when test="${totArticles > 100 }">
					<c:forEach var="page" begin="1" end="10" step="1">
						<c:if test="${section > 1 &&  page==1 }">
						<a class="no-uline" href="${contextPath }/reviewBoard/review_listArticles.do?section=${section}&pageNum=${(section-1)*10+1}">&nbsp;pre</a>
						</c:if>
						<a class="no-uline" href="${contextPath }/reviewBoard/review_listArticles.do?section=${section }&pageNum=${page }">${(section-1)*10 + page }</a>
						<c:if test="${page == 10 }">
						<a class="no-uline" href="${contextPath }/reviewBoard/review_listArticles.do?section=${section+1 }&pageNumm=${section*10+1 }">&nbsp;next</a>
						</c:if>					
					</c:forEach>
				</c:when>
				<c:when test="${totArticles == 100 }">
					<c:forEach	var="page" begin="1" end="10" step="1">
						<a class="no-uline" href="#">{page}</a>
					</c:forEach>
				</c:when>
				<c:when test="${totArticles < 100 }">
				<c:forEach var="page" begin="1" end="${totArticles/10 +1 }" step="1">
					<c:choose>
						<c:when test="${page == pageNum }">
							<a class="sel-page" href="${contextPath }/reviewBoard/review_listArticles.do?section=${section }&pageNum=${page }">${page }</a> 
						</c:when>
						<c:otherwise>
							<a class="no-uline" href="${contextPath }/reviewBoard/review_listArticles.do?section=${section }&pageNum=${page }">${page }</a>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
	</div>
	<a class="cls1" href="javascript:fn_articleForm('${isLogOn }','${contextPath }/reviewBoard/review_Form.do','${contextPath }/member/loginForm.do')">
	<p class="cls2">글쓰기</p></a>
</body>
</html>


