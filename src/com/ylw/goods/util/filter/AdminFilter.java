package com.ylw.goods.util.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 85243 on 2017/4/3.
 */
public class AdminFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getSession().getAttribute("admin")==null){
            req.setAttribute("msg","你还没有登录");
            req.getRequestDispatcher("/adminjsps/login.jsp").forward(req,response);
        }else {
            chain.doFilter(req,response);
        }
    }

    @Override
    public void destroy() {

    }
}
