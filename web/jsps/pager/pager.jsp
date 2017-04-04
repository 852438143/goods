<%@ page language="java" import="com.ylw.goods.po.Book" pageEncoding="UTF-8"%>
<%@ page import="com.ylw.goods.util.page.Pager" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	function _go() {
		var pc = $("#pageCode").val();//获取文本框中的当前页码
		if(!/^[1-9]\d*$/.test(pc)) {//对当前页码进行整数校验
			alert('请输入正确的页码！');
			return;
		}
		if(pc > ${pager.tp}) {//判断当前页码是否大于最大页
			alert('请输入正确的页码！');
			return;
		}
		location = "${pager.url}&pc="+pc;
	}
</script>


<div class="divBody">
  <div class="divContent">
    <%--上一页 --%>
        <%
            Pager pager= (Pager) request.getAttribute("pager");
            if(pager.getPc()==1){%>
                <span class="spanBtnDisabled">上一页</span>
            <%}else{%>
                        <a href="${pager.url}&pc=${pager.pc-1}" class="aBtn bold">上一页</a>
         <%   }
        %>



    <%--test里面要写el表达式,要不然判断不会执行的,-->
    <%-- 计算begin和end  通过begin和end来显示下方的页码数   --%>
      <%-- 如果总页数<=6，那么显示所有页码，即begin=1 end=${pb.tp} --%>
        <%-- 设置begin=当前页码-2，end=当前页码+3 --%>
          <%-- 如果begin<1，那么让begin=1 end=6 --%>
          <%-- 如果end>最大页，那么begin=最大页-5 end=最大页 --%>
    <c:choose>
        <c:when test="${pager.tp<=6}">
            <c:set var="begin" value="1"/>
            <c:set var="end" value="${pager.tp}"/>
        </c:when>
        <c:otherwise>
            <c:set var="begin" value="${pager.pc-2}"/>
            <c:set var="end" value="${pager.pc+3}"/>
            <c:if test="${begin<1}">
                <c:set var="begin" value="1"/>
                <c:set var="end" value="6"/>
            </c:if>
            <c:if test="${end>pager.tp}">
                <c:set var="begin" value="${pager.tp-5}"/>
                <c:set var="end" value="${pager.tp}"/>
            </c:if>
        </c:otherwise>
    </c:choose>

    <%-- 显示页码列表 --%>
        <c:forEach begin="${begin}" end="${end}" var="i">
            <c:choose>
            <c:when test="${i eq pager.pc }">
                <span class="spanBtnSelect">${i }</span>
            </c:when>
            <c:otherwise>
                <a href="${pager.url }&pc=${i}" class="aBtn">${i }</a>
            </c:otherwise>
            </c:choose>
        </c:forEach>



    <%-- 显示点点点 --%>
        <c:choose>
            <c:when test="${end != pager.tp}">
                <span class="spanApostrophe">...</span>
            </c:when>
        </c:choose>

     <%--下一页  使用标签,和上面上一页使用的方法不同  标签在jstl里面的meta-inf中的c.tld文件下--%>
        <c:choose>
           <c:when test="${pager.pc eq pager.tp}"><span class="spanBtnDisabled">下一页</span></c:when>
            <c:otherwise ><a href="${pager.url}&pc=${pager.pc+1}" class="aBtn bold">下一页</a> </c:otherwise>
        </c:choose>


    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    <%-- 共N页 到M页 --%>
    <span>共${pager.tp}页</span>
    <span>到</span>
    <input type="text" class="inputPageCode" id="pageCode" value="${pager.pc}"/>
    <span>页</span>
    <a href="javascript:_go();" class="aSubmit">确定</a>
  </div>
</div>