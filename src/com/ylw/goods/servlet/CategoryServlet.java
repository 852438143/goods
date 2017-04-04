package com.ylw.goods.servlet;

import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.service.CategoryService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by 85243 on 2017/3/29.
 */
public class CategoryServlet extends BaseServlet{
    CategoryService categoryService = new CategoryService();
    Logger log = Logger.getLogger(CategoryServlet.class);
    public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List list = categoryService.findAll();
        log.info("list size is"+ list.size());
        req.setAttribute("parents",list);
        return "f:/jsps/left.jsp";
    }
}
