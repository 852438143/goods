package com.ylw.goods.po;

/**订单条目类
 * 数据库中的bid没有定义为t_book表的外键,订单条目保存了书本的基本信息,如果书本下架,导致bid消失,会出现外键错误,所以这个bid只是单纯的保存一个字符串罢了
 * Created by 85243 on 2017/4/1.
 */
public class OrderItem {
    private String orderItemId;
    private int quantity;
    private double subtotal;//要和数据库名字一样的,要不然数据库的数据没法映射到类里面,这个和commonutils.tobean，方法有关的,数据库串过来键值对是区分大小写的
    private Book book;//这里直接用字符串表示也应该没有问题的,等项目做完回来在验证是否正确
    private String bname;
    private double currPrice;
    private String image_b;
    private Order order;


    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public double getCurrPrice() {
        return currPrice;
    }

    public void setCurrPrice(double currPrice) {
        this.currPrice = currPrice;
    }

    public String getImage_b() {
        return image_b;
    }

    public void setImage_b(String image_b) {
        this.image_b = image_b;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", book=" + book +
                ", bname='" + bname + '\'' +
                ", currPrice=" + currPrice +
                ", image_b='" + image_b + '\'' +
                ", order=" + order +
                '}';
    }
}
