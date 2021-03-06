<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>图书分类</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/list.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />
<script type="text/javascript" src="<c:url value='/jsps/pager/pager.js'/>"></script>

<script type="text/javascript" src="<c:url value='/adminjsps/admin/js/book/list.js'/>"></script>
  </head>
  
  <body>
<div class="divBook">


	<ul>
		<c:forEach items="${pager.listBean}" var="book">
			<li>
				<div class="inner">

					<a class="pic" href="<c:url value='/admin/AdminBookServlet?method=getBookByBid&bid=${book.bid}'/>"><img src="<c:url value="/${book.image_b}"/>" border="0"/></a>
					<p class="price">
						<span class="price_n">&yen;${book.currPrice }</span>
						<span class="price_r">&yen;${book.price }</span>
						(<span class="price_s">${book.discount }折</span>)
					</p>
					<p><a id="bookname" title="${book.bname}" href="<c:url value='/admin/AdminBookServlet?method=getBookByBid&bid=${book.bid}'/>">${book.bname}</a></p>
					<!--这里没有选择直接在超链接里面传url,因为中文用url传值的话在服务端接受不到数据,所以使用param标签,给url编码,看到url链接变成了一堆数字和符号-->
					<c:url var="authorUrl" value="/admin/AdminBookServlet">
						<c:param name="method" value="getPagerByAuthor"/>
						<c:param name="author" value="${book.author}"/>
					</c:url>
					<p><a href="${authorUrl}" name='P_zz' title='${book.author}'>${book.author}</a></p>
					<p class="publishing">
						<c:url var="pressUrl" value="/admin/AdminBookServlet">
							<c:param name="method" value="getPagerByPress"/>
							<c:param name="press" value="${book.press}"/>
						</c:url>
						<span>出 版 社：</span><a href="${pressUrl}">${book.press}</a>
					</p>
					<p class="publishing_time"><span>出版时间：</span>${book.publishtime}</p>
				</div>
			</li>
		</c:forEach>



	</ul>
</div>
	<div style="float:left; width: 100%; text-align: center;">
		<hr/>
		<br/>
		<%@include file="/jsps/pager/pager.jsp" %>
	</div>
 </body>
 
</html>

