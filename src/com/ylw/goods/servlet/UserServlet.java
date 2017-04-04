package com.ylw.goods.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.User;
import com.ylw.goods.service.UserService;
import com.ylw.goods.service.exception.UserServiceException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 85243 on 2017/3/25.
 */
public class UserServlet extends BaseServlet{
    Logger logger = Logger.getLogger(UserServlet.class);
    private UserService userService = new UserService();

    /**
     * 退出功能,只要是注销session
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  String quit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        return "r:/jsps/user/login.jsp";
    }

    /**
     * 修改用户信息
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  String updateLoginpass(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单信息
        User userform = CommonUtils.toBean(req.getParameterMap(),User.class);
        //从session中获取信息
        User usersession = (User) req.getSession().getAttribute("sessionUser");
        //校验   这里只做一个session校验,校验以后可以使用过滤器来完成,现在还不会,老师没讲的话,自己跟完项目学一下
        if(usersession == null){
            req.setAttribute("msg","你还没有登陆!!!");
            return "f:/jsps/user/login.jsp";
        }
        try {
            userService.updateLoginpass(usersession.getUid(),userform.getLoginpass(),userform.getNewpass());
            req.setAttribute("code","success");
            req.setAttribute("msg","修改密码成功");
            return "f:/jsps/msg.jsp";
        } catch (UserServiceException e) {
             req.setAttribute("msg",e.getMessage());
             req.setAttribute("user",userform);
            return "f:/jsps/user/pwd.jsp";
        }
    }

    /**
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        获取页面的UserBean对象
        User userform = CommonUtils.toBean(req.getParameterMap(),User.class);
        logger.info(userform);
        if(!validateLogin(userform,req)){
            req.setAttribute("msg","验证码错误!!");
            //用于数据回显
            req.setAttribute("user",userform);
            return "f:/jsps/user/login.jsp";
        }
        User user = userService.login(userform);
        if(user==null){
            req.setAttribute("msg","用户名或者密码错误!!");
            //用于数据回显
            req.setAttribute("user",userform);
            return "f:/jsps/user/login.jsp";
        }
        else {
            if(!user.isStatus()){
                req.setAttribute("msg","用户未激活!!");
                //用于数据回显
                req.setAttribute("user",userform);
                return "f:/jsps/user/login.jsp";
            }else {
                //把用户扔进session
                req.getSession().setAttribute("sessionUser",user);
                //把用户名扔进cookie
                String loginname = user.getLoginname();
                //因为cookie字符编码问题,这里存中文需要修改字符编码
                loginname = URLEncoder.encode(loginname,"utf-8");
                Cookie cookie = new Cookie("loginname",loginname);
                //设置cookie时长,默认和浏览器一样
                cookie.setMaxAge(60*60*24*10*1000);//单位是秒不是毫秒
                resp.addCookie(cookie);
                logger.info("cookie is : " +cookie);
                return "r:/index.jsp";//这里采用重定向,因为所有的req里面parameter都没用了
            }
        }
    }


    /**
     * 对login传过来的参数校验,暂不需要,等需要的时候在添加
     * @param user
     * @param
     * @return
     */
    public Boolean validateLogin(User user, HttpServletRequest req){
        if(!user.getVerifyCode().equalsIgnoreCase((String) req.getSession().getAttribute("vCode"))){
            return false ;
        }
        return true;
    }



    /**
 注册方法
 *
 */
    public  String regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        获取页面的UserBean对象
        User userForm = CommonUtils.toBean(req.getParameterMap(),User.class);
        logger.info(userForm);
//        注册校验
        Map errors = validateUser(userForm,req.getSession());
        logger.info("the error message is :"+errors);
        if(errors.size()>0){
            req.setAttribute("form",userForm);
            req.setAttribute("errors",errors);
            return "f:/jsps/user/regist.jsp";
        }
//        完成注册信息
        userService.regist(userForm);
//        成功信息转发
        req.setAttribute("code","success");
        req.setAttribute("msg","注册成功马上到邮箱激活");
        return "f:/jsps/msg.jsp";
    }

    /**
     * 邮箱激活
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String activation(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String activationCode=req.getParameter("activationCode");
        logger.info(activationCode);
        try {
            userService.activation(activationCode);
            //可以在这里面继续写成功信息的代码,因为上一步抛出异常的话,
            // 下面就不执行了,直接执行catch里面的啦,也可以把成功信息的代码写到try-catch模块外面
            req.setAttribute("code","success");
            req.setAttribute("msg","激活成功了");
        } catch (UserServiceException e) {
            //这里捕获到了 UserService里面抛出的UserviceException异常,可以进行异常处理
            req.setAttribute("code","error");
            req.setAttribute("msg",e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }
    /**
     * 对传过来的数据进行校验
     * @param user
     * @param session
     * @return
     */
    public Map<String,String> validateUser(User user, HttpSession session){
        Map<String,String> result = new HashMap();
        //对用户名进行校验
        String loginname = user.getLoginname();
        if(loginname==null||loginname.trim().equals("")){
            result.put("loginname","用户名不能为空");
        }
        else if(loginname.length()<3||loginname.length()>20){
            result.put("loginname","用户名必须在3~10个字符之间");
        }
        else if(!userService.ajaxValidateLoginname(loginname)){
            result.put("login","用户名已存在");
        }

        //密码校验
        String loginpass = user.getLoginpass();
        if(loginpass==null||loginpass.trim().equals("")){
            result.put("loginpass","密码不能为空");
        }else if(loginpass.length()<3||loginpass.length()>20){
            result.put("loginpass","密码长度必须在3~到20之间");
        }
        //确认密码校验
        String reloginpass = user.getReloginpass();
        if(reloginpass==null||reloginpass.trim().equals("")){
            result.put("reloginpass","确认密码不能为空");
        }else if(!reloginpass.equals(loginpass)){
            result.put("reloginpass","两次密码不一致");
        }
        //邮箱校验

        String email = user.getEmail();
        if(email==null|| email.trim().equals("")){
            result.put("email","邮箱不能为空");
        }else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
            result.put("email","邮箱格式错误");
        }else if(!userService.ajaxValidateEmail(email)){
            result.put("email","邮箱已被注册");
        }
        //验证码校验
        String verifyCode = user.getVerifyCode();
        String sessionvCode = (String) session.getAttribute("vCode");
        if(verifyCode==null|| verifyCode.trim().matches("")){
            result.put("verifyCode","验证码不能为空");
        }else if(!verifyCode.equalsIgnoreCase(sessionvCode)){
            result.put("verifyCode","验证码不匹配");
        }
        return result;
    }

    /**
     * 对ajax传过来的数据进行校验
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginname = req.getParameter("loginname");
        boolean b = userService.ajaxValidateLoginname(loginname);
        resp.getWriter().print(b);
        return null;
    }
    public  String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        boolean  b = userService.ajaxValidateEmail(email);
        resp.getWriter().print(b);
        return null;
    }
    public  String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String vCode = (String) req.getSession().getAttribute("vCode");
        String verifyCode = req.getParameter("verifyCode");
        logger.info("vCode is : "+vCode +" servletVerfiCode is : "+verifyCode);
        boolean b = verifyCode.equalsIgnoreCase(vCode);
        logger.info("the result is " +b);
        resp.getWriter().print(b);
        return null;
    }
}
