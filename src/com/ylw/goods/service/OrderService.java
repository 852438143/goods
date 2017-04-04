package com.ylw.goods.service;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.JdbcUtils;
import com.ylw.goods.dao.CartItemDao;
import com.ylw.goods.dao.OrderDao;
import com.ylw.goods.po.CartItem;
import com.ylw.goods.po.Order;
import com.ylw.goods.po.OrderItem;
import com.ylw.goods.util.page.Pager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 多表查询需要事务处理,个人觉得查询没必要事务,
 * Created by 85243 on 2017/4/1.
 */
public class OrderService {
    private OrderDao orderDao = new OrderDao();
    private CartItemDao cartItemDao = new CartItemDao();

    public void updateStatusByOid(String oid,int status){
        try {
            orderDao.updateStatusByOid(oid,status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getStatusByOid(String oid){
        try {
            return orderDao.getStatusByOid(oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Order getOrderByOid(String oid){
        try {
            return orderDao.getOrderByOid(oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void add(Order order,String cartItemIds){
        try {
            order.setOid(CommonUtils.uuid());
            order.setOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            order.setStatus(1);
            BigDecimal total = new BigDecimal("0");
            //每个购物车信息和每个订单条目信息对应
            List<CartItem> cartItems = cartItemDao.getCartItemsByCartItemIds(cartItemIds);
            List<OrderItem> orderItems = new ArrayList<>();
            for(CartItem cartItem : cartItems){
                total = total.add(BigDecimal.valueOf(cartItem.getSubTotal()));
                OrderItem orderItem = new OrderItem();//这个要写在for里面,写在外面,整个list所有的对象都一样了
                orderItem.setOrderItemId(CommonUtils.uuid());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubtotal(cartItem.getSubTotal());
                orderItem.setBook(cartItem.getBook());
                orderItem.setBname(cartItem.getBook().getBname());
                orderItem.setCurrPrice(cartItem.getBook().getCurrPrice());
                orderItem.setImage_b(cartItem.getBook().getImage_b());
//                orderItem.setOrder(order);//这里不能赋值,这样貌似就有了一个圈,导致 java.lang.StackOverflowErrorStackOverflowError 栈溢出
                orderItems.add(orderItem);
            }
            order.setTotal(total.doubleValue());
            order.setOrderItems(orderItems);
            JdbcUtils.beginTransaction();
            orderDao.add(order);
            //删除购物车里面的订单
            cartItemDao.batchDelete(cartItemIds);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }


    public Pager<Order> getByStatus(int status, int pc, int ps)  {
        Pager<Order> pager = new Pager<>();
        try {
            JdbcUtils.beginTransaction();
            pager = orderDao.getByStatus(status,pc,ps);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
        return pager;
    }
    public Pager<Order> getAll(int pc, int ps){
        Pager<Order> pager = new Pager<>();
        try {
            JdbcUtils.beginTransaction();
            pager = orderDao.getAll(pc,ps);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
        return pager;
    }


    public Pager<Order> getPagerByUid(String uid, int pc, int ps){
        Pager<Order> pager = new Pager<>();
        try {
            JdbcUtils.beginTransaction();
            pager = orderDao.getPagerByUid(uid,pc,ps);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        }
        return pager;
    }

}
