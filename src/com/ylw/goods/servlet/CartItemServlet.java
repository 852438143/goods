package com.ylw.goods.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.Book;
import com.ylw.goods.po.CartItem;
import com.ylw.goods.po.User;
import com.ylw.goods.service.CartItemService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**netstat  -ano |find "8080"
 * cmd中查询8080端口号对应的pid
 *
 * 主要是购物车 这里和订单有很多交互,比较麻烦的
 * Created by 85243 on 2017/3/31.
 */
public class CartItemServlet extends BaseServlet{
    private CartItemService cartItemService = new CartItemService();
    private Logger log = Logger.getLogger(CartItemServlet.class);

    /**
     * 当用户点击结算时,查询所有复选框checked=true的订单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getCartItemsByCartItemIds(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cartItemIds = req.getParameter("cartItemIds");
        double total =Double.parseDouble(req.getParameter("total"));
        log.info("cartItemIds is : "+cartItemIds);
        List<CartItem> cartItems = cartItemService.getCartItemsByCartItemIds(cartItemIds);
        log.info("carItems size is : "+cartItems.size());
        req.setAttribute("cartItems",cartItems);
        req.setAttribute("cartItemIds",cartItemIds);
        req.setAttribute("total",total);
        return "f:/jsps/cart/showitem.jsp";
    }
    /**
     * 更新图书数量,主要使用在ajax 页面的加减号
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void updateBookQuantity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cartItemId = req.getParameter("cartItemId");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        CartItem cartItem = cartItemService.updateBookQuantity(cartItemId,quantity);
        //返回json的数据格式的数据
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
        sb.append(",");
        sb.append("\"subTotal\"").append(":").append(cartItem.getSubTotal());
        sb.append("}");
        log.info(sb.toString());
        resp.getWriter().print(sb);
    }
    /**
     * 批量删除
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String batchDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cartItemIds = req.getParameter("cartItemIds");
        log.info("cartItemIds is: "+cartItemIds);
        cartItemService.batchDelete(cartItemIds);
        return getCartItemsByUid(req,resp);
    }

    /**
     * 通过用户id和图书id查询 查询是否存在这个订单,如果存在在更新该书本的数量,如果不存在,创建新的订单,
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String addCartItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map map = req.getParameterMap();
        CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        User user = (User)req.getSession().getAttribute("sessionUser");
        cartItem.setBook(book);
        cartItem.setUser(user);
        cartItemService.addCartItem(cartItem);
        return getCartItemsByUid(req,resp);
    }
    /**
     * 通过用户id查询该用户的所有订单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getCartItemsByUid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user= (User) req.getSession().getAttribute("sessionUser");
        log.info("user is: "+ user);
        String uid = user.getUid();
        log.info("uid is : "+ uid);
        List<CartItem> cartItems= cartItemService.getCartItemsByUid(uid);
        log.info("cartItems is : "+cartItems);
        req.setAttribute("cartItems",cartItems);
        return "f:/jsps/cart/list.jsp";
    }
}
