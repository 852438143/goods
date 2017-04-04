package com.ylw.goods.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.ylw.goods.po.Book;
import com.ylw.goods.po.CartItem;
import com.ylw.goods.po.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 85243 on 2017/3/31.
 */
public class CartItemDao {
    QueryRunner qr = new TxQueryRunner();
    Logger log = Logger.getLogger(CartItem.class);


    /**
     * 通过多个订单号,查询该订单,这个是在用户复选框点击后,下单后获取checked=true的订单
     * @param cartItemIds
     * @return
     * @throws SQLException
     */
    public List<CartItem> getCartItemsByCartItemIds(String cartItemIds) throws SQLException {
        Object[] params = cartItemIds.split(",");
        String insql = getInsql(params.length);
        String sql = "select * from t_book b,t_cartItem c where b.bid=c.bid and c.cartItemid ";
        sql +=insql;
        log.info(sql);
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),params);
        return toCartItems(mapList);
    }



    /**
     * 通过cartItemId查询订单
     * @param cartItemId
     * @return
     */
    public CartItem getCartItemByCid(String cartItemId) throws SQLException {
        //这里需要多表查询,因为cartItem对象的小计subTotal需要book的currPrise，所以不能仅仅包含bid
        String sql = "select * from t_book b,t_cartItem c where b.bid =c.bid and c.cartItemId =?";
        Map map = qr.query(sql,new MapHandler(),cartItemId);
        return toCartItem(map);
    }

    /**
     * 通过参数长度得到in语句 形式为in (? ,? ,?);
     * @param len
     * @return
     */
    private String getInsql(int len){
        StringBuilder sb = new StringBuilder();
        sb.append("in (");
        for(int i = 0; i<len ;i++){
            if(i == len-1){
                sb.append("?");
                break;
            }
            sb.append("?,");
        }
        sb.append(")");
        return sb.toString();
    }
    /**
     * 批量删除
     * @param cartItemIds
     */
    public void batchDelete(String cartItemIds) throws SQLException {
        Object[] params = cartItemIds.split(",");
        //WHERE column_name IN (value1,value2,...) 这个是批量的语句
        String inSql = getInsql(params.length);
        String sql = "delete from t_cartItem where cartItemid ";
        sql+=inSql;
        log.info(sql );// Wrong number of parameters: expected 0, was given 1 Query 这里出现错误,期望0个参数,我们给了一个参数
        qr.update(sql,params);//这里要的是Object数组,因为update方法用两个方法,update(String sql,Object... )
        // 和update(String sql,Object) 如果传入String[]数组对象会被当成Object对象,那么传进去的参数就是一个{'aaa','bbb'}这种类型的东西了
    }

    /**
     * 通过用户id和书id查询订单,防止用户存在同一本书,购买了多本,存在多个订单
     * @param uid
     * @param bid
     * @return
     * @throws SQLException
     */
    public CartItem getCartItemByUidAndBid(String uid,String bid) throws SQLException {
        String sql = "select * from t_cartitem where uid =? and bid= ?";
        Map<String ,Object> map = qr.query(sql, new MapHandler(),uid,bid);
        return toCartItem(map);
    }

    /**
     * 通过cartItemId更新这个订单的书本的数量
     * @param cartItemId
     * @param quantity
     */
    public void updateBookQuantity(String cartItemId, int quantity) throws SQLException {
        String sql = "update t_cartItem set quantity=? where cartItemId =?";
        qr.update(sql,quantity,cartItemId);
    }

    /**
     * 向数据库添加一条订单信息
     * @param cartItem
     * @throws SQLException
     */
    public void addCartItem(CartItem cartItem) throws SQLException {
        String sql ="insert into t_cartItem (cartItemId, quantity, bid, uid) values(?,?,?,?)";//这里需要把列名给写出来,要不然会出现列的数目不匹配问题
        Object[] params = new Object[]{cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),cartItem.getUser().getUid()};
        qr.update(sql,params);
    }

    /**
     * 通过uid获取用户的所有购物车信息
     * @param uid
     * @return
     */
    public List<CartItem> getCartItemsByUid(String uid) throws SQLException {
        //多表查询,从里面获取所有的属性,所以以后建数据库的时候,一定要每个表的属性名称都要不一样的,要不然映射的时候容易出错
        String sql = "select * from t_book book,t_cartitem cartitem where book.bid = cartitem.bid and uid=? order by cartitem.OrderBy";
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),uid);
        log.info(mapList);
        return toCartItems(mapList);
    }

    /**
     * 把一个mapList对象转化为ListCartItem对象
     * @param mapList
     * @return
     */
    private List<CartItem> toCartItems(List<Map<String,Object>> mapList){
        List<CartItem> cartItems = new ArrayList<>();
        if(mapList==null || mapList.size()==0)return null;
        for(Map<String,Object> map : mapList){
            cartItems.add(toCartItem(map));
        }
        return cartItems;
    }

    /**
     * 把一个map对象转化为CartItem对象
     * @param map
     * @return
     */
    private CartItem toCartItem(Map<String,Object> map){
        if(map == null || map.size() == 0) return null;
        log.info("the map is : "+ map);
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map,Book.class);
        User user = CommonUtils.toBean(map,User.class);
        log.info("book is : "+book);
        log.info("user is : "+ user);
        cartItem.setBook(book);
        cartItem.setUser(user);
        return cartItem;
    }
}
