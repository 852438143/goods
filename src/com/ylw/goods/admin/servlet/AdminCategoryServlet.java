package com.ylw.goods.admin.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.Category;
import com.ylw.goods.service.BookService;
import com.ylw.goods.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 因为权限问题,不可以将前台和后台公用一个servlet,防止越权
 * Created by 85243 on 2017/4/2.
 */
public class AdminCategoryServlet extends BaseServlet{
    CategoryService categoryService = new CategoryService();

    public String deleteChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cid = req.getParameter("cid");
        BookService bookService = new BookService();
        if(bookService.countBookByCid(cid)!=0){
            req.setAttribute("code","error");
            req.setAttribute("msg","当前分类不可删除");
            return "f:/adminjsps/msg.jsp";
        }
        categoryService.delete(cid);
        return findAll(req,resp);
    }

    public String deleteParent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cid = req.getParameter("cid");
        System.out.println(categoryService.countChildsByParent(cid));
        if(categoryService.countChildsByParent(cid)!=0){
            req.setAttribute("code","error");
            req.setAttribute("msg","当前分类不可删除");
            return "f:/adminjsps/admin/msg.jsp";
        }
        categoryService.delete(cid);
        return findAll(req,resp);
    }

    public String editChildPrepare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cid = req.getParameter("cid");
        String pid = req.getParameter("pid");
        Category child = categoryService.get(cid);
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);
        List<Category> parents = categoryService.getAllParents();
        req.setAttribute("child",child);
        req.setAttribute("parents",parents);
        return "f:/adminjsps/admin/category/edit2.jsp";
    }
    public String editChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category =CommonUtils.toBean(req.getParameterMap(),Category.class);
        String pid = req.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        category.setParent(parent);
        categoryService.edit(category);
        return findAll(req,resp);
    }


    public String editParentPrepare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cid = req.getParameter("cid");
        Category parent = categoryService.get(cid);
        req.setAttribute("parent",parent);
        return "f:/adminjsps/admin/category/edit.jsp";
    }

    public String editParent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category parent = CommonUtils.toBean(req.getParameterMap(),Category.class);
        System.out.println(parent);
        categoryService.edit(parent);
        return findAll(req,resp);
    }

    public String addParent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = CommonUtils.toBean(req.getParameterMap(),Category.class);
        category.setCid(CommonUtils.uuid());
        categoryService.add(category);
        return findAll(req,resp);
    }

    public String addChildPrepare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cid = req.getParameter("cid");
        String cname = req.getParameter("cname");
        List<Category> categorys = categoryService.getAllParents();
        req.setAttribute("pid",cid);
        req.setAttribute("pname",cname);
        req.setAttribute("parents",categorys);
        return "f:/adminjsps/admin/category/add2.jsp";
    }
    public String addChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category child = CommonUtils.toBean(req.getParameterMap(),Category.class);
        String pid = req.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        child.setCid(CommonUtils.uuid());
        child.setParent(parent);
        categoryService.add(child);
        return findAll(req,resp);
    }

    /**
     * 和findCategorysAll方法一样,只是结果跳转的页面不一样罢了
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category > categorys = categoryService.findAll();
        req.setAttribute("parents",categorys);
        return "f:/adminjsps/admin/category/list.jsp";
    }
    public String findCategorysAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category > categorys = categoryService.findAll();
        req.setAttribute("parents",categorys);
        return "f:/adminjsps/admin/book/left.jsp";
    }
}
