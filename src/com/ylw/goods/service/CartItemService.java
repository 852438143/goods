package com.ylw.goods.service;

import cn.itcast.commons.CommonUtils;
import com.ylw.goods.dao.CartItemDao;
import com.ylw.goods.po.CartItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 85243 on 2017/3/31.
 */
public class CartItemService {
    CartItemDao cartItemDao = new CartItemDao();



    public List<CartItem> getCartItemsByCartItemIds(String cartItems){
        try {
            return cartItemDao.getCartItemsByCartItemIds(cartItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 批量删除
     * @param cartItemids
     */
    public void batchDelete(String cartItemids){
        try {
            cartItemDao.batchDelete(cartItemids);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新订单book数量
     * @param cartIemId
     * @param quantity
     * @return
     */
    public CartItem updateBookQuantity(String cartIemId,int quantity){
        try {
            cartItemDao.updateBookQuantity(cartIemId,quantity);
            return cartItemDao.getCartItemByCid(cartIemId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 首先查看该用户是否有购买这本书的订单,如果存在直接更新该订单,如果不存在,就新建订单
     */
    public void addCartItem(CartItem cartItem){
        try {
            CartItem oldCartItem = cartItemDao.getCartItemByUidAndBid(cartItem.getUser().getUid(),cartItem.getBook().getBid());
            if(oldCartItem==null ){
                cartItem.setCartItemId(CommonUtils.uuid());
                cartItemDao.addCartItem(cartItem);
            }else{
                cartItemDao.updateBookQuantity(oldCartItem.getCartItemId(),oldCartItem.getQuantity()+cartItem.getQuantity());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 通过uid获取该用户的订单信息
     * @param uid
     * @return
     */
    public List<CartItem> getCartItemsByUid(String uid){
        try {
            return cartItemDao.getCartItemsByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
