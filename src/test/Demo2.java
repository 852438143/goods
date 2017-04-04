package test;

import com.ylw.goods.po.Order;
import com.ylw.goods.service.OrderService;
import org.junit.Test;

/**
 * Created by 85243 on 2017/4/1.
 */
public class Demo2 {
    @Test
    public void test1(){
        OrderService orderService = new OrderService();
        System.out.println(orderService.getPagerByUid("531D8A16D524478D86F8A115FE95D93F",1,4));
    }
    @Test
    public void test2(){
        OrderService orderService = new OrderService();
        Order order = new Order();
        orderService.add(order,"3AB0C9264F9E4ED1BB9ED3790121259C");
        System.out.println(order);
    }

}
