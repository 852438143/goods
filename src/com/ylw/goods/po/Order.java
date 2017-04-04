package com.ylw.goods.po;

import java.util.List;

/** 订单类,
 * 和订单条目类双向关联,是一对多的关系,有点类似分类的那个类category,订单类类似于一级分类,订单条目类类似于二级分类
 *
 * Created by 85243 on 2017/4/1.
 */
public class Order {
    private String oid;
    private String orderTime;
    private double total;
    private int status;//订单状态：1未付款, 2已付款但未发货, 3已发货未确认收货, 4确认收货了交易成功, 5已取消(只有未付款才能取消)
    private String address;
    private User user;
    private List<OrderItem> orderItems;


    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid='" + oid + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", total=" + total +
                ", status=" + status +
                ", address='" + address + '\'' +
                ", user='" + user + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
}
