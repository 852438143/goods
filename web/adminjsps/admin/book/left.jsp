<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'left.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/left.css'/>">
      <script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
	<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>" type="text/css" media="all">
      <script language="javascript">
          var bar = new Q6MenuBar("bar", "传智播客网上书城");
      $(function() {
      bar.colorStyle = 2;
      bar.config.imgDir = "<c:url value='/menu/img/'/>";
      bar.config.radioButton=true;  //一级分类是否排斥

      /**
      * 	bar.add("程序设计", "Java Javascript", "/goods/jsps/book/list.jsp", "body");
      * 第一个参数:一级分类名称
      * 2:     二级分类名称
      * 3:     点击二级分类后链接到的url
      * 4：    链接的内容在哪个框架中显示,,,body是在main.jsp中定义的  <iframe>中name
      */
      <c:forEach items="${parents}" var="parent" >/*貌似标签不用特殊处理,可能和js有关系,不太清楚*/
      <c:forEach items="${parent.children}" var="child">
         bar.add("${parent.cname}","${child.cname}","/goods/admin/AdminBookServlet?method=getPagerByCatogory&cid=${child.cid}","body");/*但是el需要加上双引号,很奇怪的*/
      </c:forEach>
      </c:forEach>



      $("#menu").html(bar.toString());
      });
      </script>
 </head>
  
  <body onload="load()">
<div id="menu"></div>
  </body>
</html>
