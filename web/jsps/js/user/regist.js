$(function() {
	// 注册按钮图片切换
	$("#submit").hover(
		function() {
			$("#submit").attr("src", "/goods/images/regist2.jpg");
		}, 
		function() {
			$("#submit").attr("src", "/goods/images/regist1.jpg");
		}
	);
	// 默认隐藏所有错误信息
	$(".error").css("display", "none");
	
	// 当提交表单时
	$("#registForm").submit(function() {//当这个返回为true时 才执行submit操作
		var bool = true;
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
	});
});

// 校验用户名
function validateLoginname() {
	$("#loginnameError").css("display", "none");
	var bool = true;
	var val = $("#loginname").val();
	if(!val) {
		$("#loginnameError").text("用户名不能为空！");
		$("#loginnameError").css("display", "");
		bool = false;
	} else if(val.length < 2 || val.length > 10) {
		$("#loginnameError").text("用户名长度必须在2~10之间！");
		$("#loginnameError").css("display", "");		
		bool = false;
	}

	//注册校验
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateLoginname",loginname:val},
		type:"POST",
		dataType:"json",
		async:false,//这里关闭异步,如果开启异步的话,那么会跳过success里面的函数,直接进行下面的,导致无论服务器传过来什么数据,对bool影响不大
		cache:false,
		success:function (result) {
			// alert(result);
			if(!result){
				$("#loginnameError").text("用户名已经注册过了");
				$("#loginnameError").css("display", "");
				bool = false;
			}
		}
		});
	return bool;
}

// 校验密码
function validateLoginpass() {
	$("#loginpassError").css("display", "none");
	var bool = true;
	var val = $("#loginpass").val();
	if(!val) {
		$("#loginpassError").text("密码不能为空！");
		$("#loginpassError").css("display", "");
		bool = false;
	} else if(val.length < 2 || val.length > 10) {
		$("#loginpassError").text("密码长度必须在2~10之间！");
		$("#loginpassError").css("display", "");		
		bool = false;
	}
	return bool;	
}

// 验证确认密码
function validateReloginpass() {
	$("#reloginpassError").css("display", "none");
	var bool = true;
	var val = $("#reloginpass").val();
	if(!val) {
		$("#reloginpassError").text("密码不能为空！");
		$("#reloginpassError").css("display", "");
		bool = false;
	} else if(val != $("#loginpass").val()) {
		$("#reloginpassError").text("两次密码输入不一致！");
		$("#reloginpassError").css("display", "");		
		bool = false;
	}
	return bool;	
}

// 校验email
function validateEmail() {
	$("#emailError").css("display", "none");
	var bool = true;
	var val = $("#email").val();
	if(!val) {
		$("#emailError").text("Email不能为空！");
		$("#emailError").css("display", "");
		bool = false;
	} else if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(val)) {
		$("#emailError").text("错误的Email格式！");
		$("#emailError").css("display", "");		
		bool = false;
	}
	//注册校验
	$.ajax({
			url:"/goods/UserServlet",
			data:{method:"ajaxValidateEmail",email:val},
			type:"POST",
			dataType:"json",
			async:false,//这里关闭异步,如果开启异步的话,那么会跳过success里面的函数,直接进行下面的,导致无论服务器传过来什么数据,对bool影响不大
			cache:false,
			success:function (result) {
				 // alert(result);
				if(!result){
					$("#emailError").text("蠢货,邮箱已经被注册了！");
					$("#emailError").css("display", "");
					bool = false;
				}
			}
		}
	)
	return bool;
}

// 校验验证码
function validateVerifyCode() {
	$("#verifyCodeError").css("display", "none");
	var bool = true;
	var val = $("#verifyCode").val();
	if(!val) {
		$("#verifyCodeError").text("验证码不能为空！");
		$("#verifyCodeError").css("display", "");
		bool = false;
	}
	//注册校验
	$.ajax({
			url:"/goods/UserServlet",
			data:{method:"ajaxValidateVerifyCode",verifycode:val},
			type:"POST",
			dataType:"json",
			async:false,//这里关闭异步,如果开启异步的话,那么会跳过success里面的函数,直接进行下面的,导致无论服务器传过来什么数据,对bool影响不大
			cache:false,
			success:function (result) {
				if(!result){
					$("#verifyCodeError").text("验证码错误");
					$("#verifyCodeError").css("display", "");
					bool = false;
				}
			}
		}
	)
	return bool;
}