package com.ylw.goods.util.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 85243 on 2017/3/30.
 */
public class EncodeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //因为MyRequest类是HttpServletRequest类的装饰器,所以把request作为参数传给MyRequest对象,目测这样应该可以的,如果不可以应该在jsp页面把字符encode在进来就行了,
        //嗯!应该就是这样的
        MyRequest req = new MyRequest((HttpServletRequest) request);
        req.setCharacterEncoding("utf-8");//貌似post方法需要处理为这样才可以,要不然post里面的中文数据会乱码
        /*
        * web服务器根据Filter在web.xml文件中的注册顺序，决定先调用哪个Filter，
        * 当第一个Filter的doFilter方法被调用时，web服务器会创建一个代表Filter链的FilterChain对象传递给该方法
        * 。在doFilter方法中，开发人员如果调用了FilterChain对象的doFilter方法，则web服务器会检查FilterChain对象中是否还有filter，
        * 如果有，则调用第2个filter，如果没有，则调用目标资源。*/
        chain.doFilter(req,response);
    }

    @Override
    public void destroy() {

    }
}
