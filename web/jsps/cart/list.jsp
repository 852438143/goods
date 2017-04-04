<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>cartlist.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
	$(function () {
		showTotal();
        //全选按钮 如果checked则所有的check全选,结算也可点击,否则相反
        $("#selectAll").click(function () {
//            alert(this);//这个显示的是[object HTMLInputElement]  这个this应该是正个页面的,单独获取这个元素的应该是$(this)
//            alert($("#selectAll"));//[object Object]
            var bool = $("#selectAll").attr("checked");
            setAllItemCheckBox(bool);
            setJieSuan(bool);
//            alert(bool);
            showTotal();
        });
//        	给所有条目的复选框添加click事件
        $(":checkbox[name=checkboxBtn]").click(function () {
            var all = $(":checkbox[name=checkboxBtn]").length;
            var selected = $(":checkbox[name=checkboxBtn][checked=true]").length;
            if(all == selected){
                $("#selectAll").attr("checked",true);
                setJieSuan(true);
            }else if(selected == 0){
                $("#selectAll").attr("checked",false);
                setJieSuan(false);
            }else{
                $("#selectAll").attr("checked",false);
                setJieSuan(true);
            }
            showTotal();
        });
        //给所有的"－"添加click事件
        $(".jian").click(function () {
            //获取当前减号所在的cartItem的id
            var id = $(this).attr("id").substring(0,32);
            //获取数量
            var quantity  = $("#"+id+"Quantity").val();
            if(quantity ==1) {
                if (confirm("是否删除该条目!!")) {
                    location = "/goods/CartItemServlet?method=batchDelete&cartItemIds=" + id;
                }
            }else{
                updateBookQuantity(id,quantity-1);
            }
            showTotal();
        });
        //给所有的"+"添加click事件
        $(".jia").click(function () {
            var id = $(this).attr("id").substring(0,32);
            var quantity  = $("#"+id+"Quantity").val();
            updateBookQuantity(id,Number(quantity)+1);//这里面的+会变成加字符串,-没有这个顾虑
            showTotal();
        });
	});
	//计算总计
	function showTotal() {
//		alert("11");
		var total = 0;
		$(":checkbox[name=checkboxBtn][checked=true]").each(function () {
			//选择复选框的值
			var id=$(this).val();
			//通过前缀找到小计元素,获取其文本
			var text = $("#"+id+"Subtotal").text();
			//累加计算
			total +=Number(text);
		})
        //因为进制问题,所以需要引入/js/round.js文件,进行四舍五入
		$("#total").text(round(total,2));
	}
	//设置结算按钮
	function setJieSuan(bool) {
		if(bool){
            $("#jiesuan").removeClass("kill").addClass("jiesuan");
            $("#jiesuan").unbind("click");//撤消当前元素止所有click事件,这个直接使用click(不加双引号)会出现效果,但是后面的语句就出错了,不知道为什么,等理解了jquery在来深入:click和"click"区别
		}else{
			$("#jiesuan").removeClass("jiesuan").addClass("kill");
            $("#jiesuan").click(false);
		}
	}
	//统一设置所有条目的复选按钮
    function setAllItemCheckBox(bool) {
       $(":checkbox[name=checkboxBtn]").attr("checked",bool);
    }
	/**
	 * 批量删除
	 */
	function batchDelete() {
		var cartItemIds = new Array();
		$(":checkbox[name=checkboxBtn][checked=true]").each( function () {
					cartItemIds.push($(this).val());
				});
//        alert(cartItemIds.length);
		location = "/goods/CartItemServlet?method=batchDelete&cartItemIds="+cartItemIds;
	}
	//ajax更新book数量
    function updateBookQuantity(id,quantity) {
//        alert(id);
        $.ajax({
            url:"/goods/CartItemServlet?method=updateBookQuantity",
            data:{method:"updateBookQuantity",cartItemId:id,quantity:quantity},
            type:"post",
            async:false,
            cache:false,
            dataType:"json",
            success:function (result) {
                $("#"+id+"Quantity").val(result.quantity);
                $("#"+id+"Subtotal").text(result.subTotal);
            }
        })
    }
    function jiesuan() {

        var cartItemIds = new Array();
        $(":checkbox[name=checkboxBtn][checked=true]").each(function () {
//            alert($(this).val());
           cartItemIds.push($(this).val());
        });
//        设置表单里面的值
        $("#cartItemIds").val(cartItemIds.toString());
//        alert(cartItemIds.toString());
        $("#hiddenTotal").val($("#total").text());
//        alert($("#total").text()); //需要加强下jquery
        $("#jiesuanForm").submit();
    }
</script>
  </head>
  <body>

<c:choose>
    <c:when test="${empty cartItems}">
        <table width="95%" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td align="right">
                    <img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
                </td>
                <td>
                    <span class="spanEmpty">您的购物车中暂时没有商品</span>
                </td>
            </tr>
        </table>
    </c:when>
    <c:otherwise>






<table width="95%" align="center" cellpadding="0" cellspacing="0">
	<tr align="center" bgcolor="#efeae5">
		<td align="left" width="50px">
			<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
		</td>
		<td colspan="2">商品名称</td>
		<td>单价</td>
		<td>数量</td>
		<td>小计</td>
		<td>操作</td>
	</tr>



	<c:forEach items="${cartItems}" var="cartItem">
	<tr align="center">
		<td align="left">
			<input value="${cartItem.cartItemId }" type="checkbox" name="checkboxBtn" checked="checked"/>
		</td>
		<td align="left" width="70px"><%----%>
			<a class="linkImage" href="<c:url value='/BookServlet?method=getBookByBid&bid=${cartItem.book.bid}'/>"><img border="0" width="54" align="top" src="<c:url value='/${cartItem.book.image_b}'/>"/></a>
		</td>
		<td align="left" width="400px">
		    <a href="<c:url value='/BookServlet?method=getBookByBid&bid=${cartItem.book.bid}'/>"><span>${cartItem.book.bname}</span></a>
		</td>
		<td><span>&yen;<span class="currPrice" id="">${cartItem.book.currPrice}</span></span></td>
		<td>
			<a class="jian" id="${cartItem.cartItemId }Jian"></a><input class="quantity" readonly="readonly" id="${cartItem.cartItemId }Quantity" type="text" value="${cartItem.quantity }"/><a class="jia" id="${cartItem.cartItemId }Jia"></a>
		</td>
		<td width="100px">
			<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subTotal}</span></span>
		</td>
		<td>
			<a href="<c:url value='/CartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId}'/>">删除</a>
		</td>
	</tr>
		</c:forEach>


	<tr>
		<td colspan="4" class="tdBatchDelete">
			<a href="javascript:batchDelete()">批量删除</a>
		</td>
		<td colspan="3" align="right" class="tdTotal">
			<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<a href="javascript:jiesuan();" id="jiesuan" class="jiesuan"></a><!--这个脚本有;不可以忘记了-->
		</td>
	</tr>
</table>
    </c:otherwise>
</c:choose>
	<form id="jiesuanForm" action="<c:url value='/CartItemServlet'/>" method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds"/>
		<input type="hidden" name="method" value="getCartItemsByCartItemIds"/>
        <input type="hidden" name="total" id="hiddenTotal"/>
	</form>


  </body>
</html>
