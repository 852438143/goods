package com.ylw.goods.po;

import java.math.BigDecimal;

/**
 * Created by 85243 on 2017/3/31.
 */
public class CartItem {
    private String cartItemId;
    private int quantity;
    private Book book;
    private User user;

    /**
     * 一个小计方法,通过quantity和book.currPrice计算价格
     * 因为钱的问题,不能用double,2-1.1=0.89999因为二进制对0.1无法表示
     * @return
     */
    public double getSubTotal(){
        BigDecimal quantity = new BigDecimal(this.quantity+"");//貌似就是string类型的构造参数准
        BigDecimal currPrice = new BigDecimal(this.book.getCurrPrice()+"");
        return quantity.multiply(currPrice).doubleValue();
    }


    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId='" + cartItemId + '\'' +
                ", quantity=" + quantity +
                ", book=" + book +
                ", user=" + user +
                '}';
    }
}
