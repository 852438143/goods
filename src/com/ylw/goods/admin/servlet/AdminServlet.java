package com.ylw.goods.admin.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.admin.po.Admin;
import com.ylw.goods.admin.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 85243 on 2017/4/2.
 */
public class AdminServlet extends BaseServlet{
    private AdminService adminService = new AdminService();
    public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin form = CommonUtils.toBean(req.getParameterMap(),Admin.class);
        Admin admin =  adminService.getAdmin(form.getAdminname(),form.getAdminpwd());
        if(admin==null){
            req.setAttribute("msg","账号或者密码错误");
            return "f:/adminjsps/login.jsp";
        }
        req.getSession().setAttribute("admin",admin);
        return "f:/adminjsps/admin/index.jsp";
    }
}
