package com.ylw.goods.admin.servlet;

import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.Order;
import com.ylw.goods.service.OrderService;
import com.ylw.goods.util.page.Pager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 85243 on 2017/4/3.
 */
public class AdminOrderServlet  extends BaseServlet{
    private OrderService orderService = new OrderService();


    public String getOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        String btn = req.getParameter("btn");
        Order order = orderService.getOrderByOid(oid);
        req.setAttribute("order",order);
        req.setAttribute("btn",btn);
        return "f:/adminjsps/admin/order/desc.jsp";
    }

    public String cancelOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        int status = orderService.getStatusByOid(oid);
        if(status !=1 ){
            req.setAttribute("code","error");
            req.setAttribute("msg","该订单无法取消");
        }else {
            orderService.updateStatusByOid(oid,5);
            req.setAttribute("code","success");
            req.setAttribute("msg","订单取消成功");
        }
        return "f:/adminjsps/msg.jsp";
    }
    public String deliver(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        int status = orderService.getStatusByOid(oid);
        if(status !=2 ){
            req.setAttribute("code","error");
            req.setAttribute("msg","未付款不能发货");
        }else {
            orderService.updateStatusByOid(oid,3);
            req.setAttribute("code","success");
            req.setAttribute("msg","发货成功");
        }
        return "f:/adminjsps/msg.jsp";
    }
    public String getByStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int ps = 8;//pagesize 这次固定了,懒得写了
        int pc = getPc(req)  ;
        String url = getUrl(req);
        int status = Integer.parseInt(req.getParameter("status"));
        Pager<Order> pager = orderService.getByStatus(status,pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/order/list.jsp";
    }
    public String getAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int ps = 8;//pagesize 这次固定了,懒得写了
        int pc = getPc(req)  ;
        String url = getUrl(req);
        Pager<Order> pager = orderService.getAll(pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/order/list.jsp";
    }

    private int getPc(HttpServletRequest req){
        int pc = 1;
        if(req.getParameter("pc")!=null&&!req.getParameter("pc").trim().equals("")){
            pc = Integer.parseInt(req.getParameter("pc"));
        }
        return pc;
    }
    private String getUrl(HttpServletRequest req){
        String url = req.getRequestURI()+"?"+req.getQueryString();
        int index=url.lastIndexOf("&pc=");
        if(index !=-1){
            url=url.substring(0,index);
        }
        return url;
    }

}
