<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 85243
  Date: 2017/3/25
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <title></title>
    <!--关于href和src的区别, src是在浏览器下载，编译，执行这个文件之前页面的加载和处理会被暂停,可以认为是同步的,所以一般把js文件放在后面,让页面先加载
    而href是并发的-->
    <link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/myregist.css'/>">
    <script type="text/javascript" src="<c:url value="/jquery/jquery-1.5.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/jsps/js/user/myregist.js"/> "></script>
</head>
<body>
<div id="divMain">
    <div id="divTitle"><span id="spanTitle">用户注册:</span></div>
    <div id="divBody">
        <form action="<c:url value="/UserServlet"/>">
            <input type="hidden" name="method" value="regist">
            <table id="tableBody">
                <tr>
                    <td class="tdText">用户名:</td>
                    <td class="tdInput"><input type="text" class="input" id="name"></td>
                    <td class="tdError"><label class="labelError" id="nameLabel">用户名不能为空</label></td>
                </tr>
                <tr>
                    <td class="tdText">登录密码:</td>
                    <td class="tdInput"><input type="text" class="input" id="password"></td>
                    <td class="tdError"><label class="labelError" id="passwordLabel">密码不能为空</label></td>
                </tr>
                <tr>
                    <td class="tdText">确认密码:</td>
                    <td class="tdInput"><input type="text" class="input" id="repassword"></td>
                    <td class="tdError"><label class="labelError" id="repasswordLabel">确认密码不能为空</label></td>
                </tr>
                <tr>
                    <td class="tdText">Email:</td>
                    <td class="tdInput"><input type="text" class="input" id="Email"></td>
                    <td class="tdError"><label class="labelError" id="EmailLabel">Email不能为空</label></td>
                </tr>
                <tr>
                    <td class="tdText">图形验证码:</td>
                    <td class="tdInput"><input type="text" class="input" id="verifyCode"/></td>
                    <td class="tdError"><label class="labelError" id="verifyCodeLabel">图形验证码不能为空</label></td>
                </tr>
                <tr>
                    <td class="tdText"></td>
                    <td>
                        <div align="center"><a href="javascript:_hyz()"><img id="imgVcode" src="/goods/VerifyCode"/></a>
                        </div>
                    </td>

                </tr>
                <tr>
                    <td class="tdText"></td>
                    <td class="tdInput"><input type="image" src="<c:url value='/images/regist1.jpg'/>"/></td><!--这里通过jsp标签构造一个url链接,
                                                还有这个input type=image 可以简化不用<a><img/></a>来构造一个可操作的图片 这里自带自动提交的-->
                    <td class="tdError"></td>
                </tr>
            </table>
        </form>
    </div>
</div>

</body>
</html>
