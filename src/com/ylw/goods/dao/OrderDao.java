package com.ylw.goods.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.ylw.goods.po.Book;
import com.ylw.goods.po.Order;
import com.ylw.goods.po.OrderItem;
import com.ylw.goods.util.page.Expression;
import com.ylw.goods.util.page.Pager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 85243 on 2017/4/1.
 */
public class OrderDao {
    private Logger log = Logger.getLogger(OrderDao.class);
    private QueryRunner qr= new TxQueryRunner();

    /**
     * 查询订单状态
     * @param oid
     * @return
     * @throws SQLException
     */
    public int getStatusByOid(String oid) throws SQLException {
        String sql = "select status from t_order where oid = ?";
        Number status  =  (Number) qr.query(sql,new ScalarHandler(),oid);
        return status.intValue();
    }

    /**
     * 修改订单状态
     * @param oid
     * @param status
     * @throws SQLException
     */
    public void updateStatusByOid(String oid ,int status) throws SQLException {
        String sql = "update t_order set status=? where oid =?";
        qr.update(sql,status,oid);
    }

    /**
     * 查询订单信息
     * @param oid
     * @return
     */
    public Order getOrderByOid(String oid) throws SQLException {
        String sql= "select * from t_order where oid = ?";
        Order order = qr.query(sql,new BeanHandler<Order>(Order.class),oid);
        setOrderItem(order);
        return order;
    }

    /**
     * 创建订单信息
     * @param order
     * @throws SQLException
     */
    public void add(Order order) throws SQLException {

        String sql= "insert into t_order values(?,?,?,?,?,?)";
        Object[] params = {order.getOid(),order.getOrderTime(),order.getTotal(),order.getStatus(),order.getAddress(),order.getUser().getUid()};
        qr.update(sql,params);

        //执行批处理,添加订单条目
        sql = "insert into t_orderItem values(?,?,?,?,?,?,?,?)";
        int len = order.getOrderItems().size();
        Object[][] paramss = new Object[len][];
        for(int i=0;i<len ;i++){
            OrderItem orderItem = order.getOrderItems().get(i);
            paramss[i] = new Object[]{orderItem.getOrderItemId(),orderItem.getQuantity(),orderItem.getSubtotal(),
                    orderItem.getBook().getBid(),orderItem.getBname(),orderItem.getCurrPrice(),orderItem.getImage_b(),order.getOid()};
        }
        qr.batch(sql,paramss);
    }

    /**
     * 查询所有的订单
     * @param pc
     * @param ps
     * @return
     * @throws SQLException
     */
    public Pager<Order> getAll( int pc, int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        Pager<Order> pager = getByCriteria(expressions,pc,ps);
        return pager;
    }

    public Pager<Order> getByStatus(int status, int pc, int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("status","=",""+status));
        Pager<Order> pager = getByCriteria(expressions,pc,ps);
        return pager;
    }
    /**
     * 通过uid查询订单信息
     * @param uid
     * @param pc
     * @param ps
     * @return
     * @throws SQLException
     */
    public Pager<Order> getPagerByUid(String uid, int pc, int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("uid","=",uid));
        Pager<Order> pager = getByCriteria(expressions,pc,ps);
        return pager;
    }

    /**
     * 分页查询一个订单信息
     * @param expressions
     * @param pc
     * @param ps
     * @return
     */
    public Pager<Order> getByCriteria(List<Expression> expressions ,int pc,int ps) throws SQLException {
        String sql = "select count(*) from t_order";
        List<Object> params = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(" where 1=1 ");
        if(expressions!=null&&expressions.size()!=0){
            for(Expression expression : expressions){
                sb.append(" and ").append(expression.getName()).append(" ").append(expression.getOperator());
                if(!expression.getOperator().equals("is null")){
                     sb.append(" ?");
                    params.add(expression.getValue());
                }
            }
        }
        Number trNum = (Number) qr.query(sql+sb.toString(),new ScalarHandler(),params.toArray());
        int tr = trNum.intValue();
        log.info("tr is : "+ tr);
        sql = "select * from t_order "+sb.toString()+" order by ordertime desc limit ?,?";
        params.add((pc-1)*ps);
        params.add(ps);
        List<Order> orders = qr.query(sql,new BeanListHandler<Order>(Order.class),params.toArray());
        //把orderItem加入到订单信息中
        for(Order order: orders){
            setOrderItem(order);
        }
        Pager<Order> pager = new Pager<>();
        pager.setTr(tr);
        pager.setPc(pc);
        pager.setPs(ps);
        pager.setListBean(orders);
        System.out.println(pager);
        return pager;
    }

    /**
     * 把订单条目设置到订单中去
     * @param order
     * @throws SQLException
     */
    private void setOrderItem(Order order) throws SQLException {
        String sql= "select * from t_orderItem where oid =?";
        List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),order.getOid());
        order.setOrderItems(toOrderItems(mapList));
    }
    private List<OrderItem> toOrderItems(List<Map<String,Object>> mapList){
        List<OrderItem> orderItems = new ArrayList<>();
        if(mapList!=null&&mapList.size()>0){
            for(Map<String,Object> map : mapList){
                orderItems.add(toOrderItem(map));
            }
        }
        return orderItems;
    }
    private OrderItem toOrderItem(Map<String,Object> map){
        OrderItem orderItem = CommonUtils.toBean(map,OrderItem.class);
        Book book = CommonUtils.toBean(map,Book.class);
        Order order = CommonUtils.toBean(map,Order.class);
        orderItem.setBook(book);
        orderItem.setOrder(order);
        return orderItem;
    }
}
