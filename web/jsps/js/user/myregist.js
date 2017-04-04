/**
 * Created by 85243 on 2017/3/26.
 */
$(function () {
    $(".labelError").css("display","none");
    changeRegistImage($(":input[type=image]"));
    // 检测每个input框中是否有内容
    $(".input").focus(function () {
        showLabelErrorByInputId($(this));
    })
    $(".input").blur(function () {
        showLabelErrorByInputId($(this));
    })
    $("form").submit(function () {
        var bool = true;//表示校验通过
        //不使用逻辑运算,因为要保证输出所有的错误信息,逻辑运算&&只要一个不满足就return了
        if(!validateLoginname()) {
            bool = false;
        }
        if(!validateLoginpass()) {
            bool = false;
        }
        if(!validateReloginpass()) {
            bool = false;
        }
        if(!validateEmail()) {
            bool = false;
        }
        if(!validateVerifyCode()) {
            bool = false;
        }

        return bool;
    })
});
// 通过inputId内容判断是否显示错误标签
function showLabelErrorByInputId(ele) {
    var text = ele.val();
    var inputId = ele.attr("id");
     // alert(inputId)
    if (text){
        $("#"+inputId+"Label").css("display","none")
        // alert($("#"+inputId+"Label").text());
    }else {
        $("#"+inputId+"Label").css("display","");//这里不能写成display "true"貌似这个值不支持,写进去这个css还是不显示
        // alert($("#"+inputId+"Label").text());
    }
}
//换一张验证码
function _hyz(){
    $("#imgVcode")[0].src="/goods/VerifyCode?a="+new Date().getTime();
}
//鼠标更改注册图片
function changeRegistImage(ele){
    $(ele).hover(function () {
        $(ele).attr("src","/goods/images/regist2.jpg");
    },function () {
        $(ele).attr("src","/goods/images/regist1.jpg");
    });
}
// 校验用户名
function validateLoginname() {
    $("#nameLabel").css("display", "none");
    var bool = true;
    var val = $("#name").val();
    if(!val) {
        $("#nameLabel").text("用户名不能为空！");
        $("#nameLabel").css("display", "");
        bool = false;
    } else if(val.length < 2 || val.length > 10) {
        $("#nameLabel").text("用户名长度必须在2~10之间！");
        $("#nameLabel").css("display", "");
        bool = false;
    }
    return bool;
}

// 校验密码
function validateLoginpass() {
    $("#passwordLabel").css("display", "none");
    var bool = true;
    var val = $("#password").val();
    if(!val) {
        $("#passwordLabel").text("密码不能为空！");
        $("#passwordLabel").css("display", "");
        bool = false;
    } else if(val.length < 2 || val.length > 10) {
        $("#passwordLabel").text("密码长度必须在2~10之间！");
        $("#passwordLabel").css("display", "");
        bool = false;
    }
    return bool;
}

// 验证确认密码
function validateReloginpass() {
    $("#repasswordLabel").css("display", "none");
    var bool = true;
    var val = $("#password").val();
    if(!val) {
        $("#repasswordLabel").text("密码不能为空！");
        $("#repasswordLabel").css("display", "");
        bool = false;
    } else if(val != $("#repassword").val()) {
        $("#repasswordLabel").text("两次密码输入不一致！");
        $("#repasswordLabel").css("display", "");
        bool = false;
    }
    return bool;
}

// 校验email
function validateEmail() {
    $("#EmailLabel").css("display", "none");
    var bool = true;
    var val = $("#Email").val();
    if(!val) {
        $("#EmailLabel").text("Email不能为空！");
        $("#EmailLabel").css("display", "");
        bool = false;
    } else if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(val)) {
        $("#EmailLabel").text("错误的Email格式！");
        $("#EmailLabel").css("display", "");
        bool = false;
    }
    return bool;
}

// 校验验证码
function validateVerifyCode() {
    $("#verifyCodeLabel").css("display", "none");
    var bool = true;
    var val = $("#verifyCode").val();
    if(!val) {
        $("#verifyCodeLabel").text("验证码不能为空！");
        $("#verifyCodeLabel").css("display", "");
        bool = false;
    }
    return bool;
}
