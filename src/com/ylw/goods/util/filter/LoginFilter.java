package com.ylw.goods.util.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 85243 on 2017/4/2.
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 注册过滤器,用来过滤用户,防止用户没有登陆,却可以访问购物车和订单信息
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getSession().getAttribute("sessionUser")==null){
            req.setAttribute("code","error");
            req.setAttribute("msg","你还没有登录,请登录");
            req.getRequestDispatcher("/jsps/msg.jsp").forward(req,response);
        }
        else{
            chain.doFilter(req,response);
        }
    }

    @Override
    public void destroy() {

    }
}
