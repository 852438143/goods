package com.ylw.goods.servlet;

import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.Order;
import com.ylw.goods.po.User;
import com.ylw.goods.service.OrderService;
import com.ylw.goods.util.PaymentUtil;
import com.ylw.goods.util.page.Pager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by 85243 on 2017/4/1.
 */
public class OrderServlet extends BaseServlet{
    private OrderService orderService  = new OrderService();


    /**
     * 支付方法
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String payment(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		/*
		 * 1. 准备13个参数
		 */
        String p0_Cmd = "Buy";//业务类型，固定值Buy
        String p1_MerId = props.getProperty("p1_MerId");//商号编码，在易宝的唯一标识
        String p2_Order = req.getParameter("oid");//订单编码
        String p3_Amt = "0.01";//支付金额
        String p4_Cur = "CNY";//交易币种，固定值CNY
        String p5_Pid = "";//商品名称
        String p6_Pcat = "";//商品种类
        String p7_Pdesc = "";//商品描述
        String p8_Url = props.getProperty("p8_Url");//在支付成功后，易宝会访问这个地址。
        String p9_SAF = "";//送货地址
        String pa_MP = "";//扩展信息
        String pd_FrpId = req.getParameter("yh");//支付通道
        String pr_NeedResponse = "1";//应答机制，固定值1

		/*
		 * 2. 计算hmac
		 * 需要13个参数
		 * 需要keyValue
		 * 需要加密算法
		 */
        String keyValue = props.getProperty("keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);

		/*
		 * 3. 重定向到易宝的支付网关
		 */
        StringBuilder sb = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
        sb.append("?").append("p0_Cmd=").append(p0_Cmd);
        sb.append("&").append("p1_MerId=").append(p1_MerId);
        sb.append("&").append("p2_Order=").append(p2_Order);
        sb.append("&").append("p3_Amt=").append(p3_Amt);
        sb.append("&").append("p4_Cur=").append(p4_Cur);
        sb.append("&").append("p5_Pid=").append(p5_Pid);
        sb.append("&").append("p6_Pcat=").append(p6_Pcat);
        sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
        sb.append("&").append("p8_Url=").append(p8_Url);
        sb.append("&").append("p9_SAF=").append(p9_SAF);
        sb.append("&").append("pa_MP=").append(pa_MP);
        sb.append("&").append("pd_FrpId=").append(pd_FrpId);
        sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
        sb.append("&").append("hmac=").append(hmac);

        resp.sendRedirect(sb.toString());
        return null;
    }
    /**
     * 回馈方法
     * 当支付成功时，易宝会访问这里
     * 用两种方法访问：
     * 1. 引导用户的浏览器重定向(如果用户关闭了浏览器，就不能访问这里了)
     * 2. 易宝的服务器会使用点对点通讯的方法访问这个方法。（必须回馈success，不然易宝服务器会一直调用这个方法）
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String back(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		/*
		 * 1. 获取12个参数
		 */
        String p1_MerId = req.getParameter("p1_MerId");
        String r0_Cmd = req.getParameter("r0_Cmd");
        String r1_Code = req.getParameter("r1_Code");
        String r2_TrxId = req.getParameter("r2_TrxId");
        String r3_Amt = req.getParameter("r3_Amt");
        String r4_Cur = req.getParameter("r4_Cur");
        String r5_Pid = req.getParameter("r5_Pid");
        String r6_Order = req.getParameter("r6_Order");
        String r7_Uid = req.getParameter("r7_Uid");
        String r8_MP = req.getParameter("r8_MP");
        String r9_BType = req.getParameter("r9_BType");
        String hmac = req.getParameter("hmac");
		/*
		 * 2. 获取keyValue
		 */
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
        String keyValue = props.getProperty("keyValue");
		/*
		 * 3. 调用PaymentUtil的校验方法来校验调用者的身份
		 *   >如果校验失败：保存错误信息，转发到msg.jsp
		 *   >如果校验通过：
		 *     * 判断访问的方法是重定向还是点对点，如果要是重定向
		 *     修改订单状态，保存成功信息，转发到msg.jsp
		 *     * 如果是点对点：修改订单状态，返回success
		 */
        boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
                r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,
                keyValue);
        if(!bool) {
            req.setAttribute("code", "error");
            req.setAttribute("msg", "无效的签名，支付失败！（你不是好人）");
            return "f:/jsps/msg.jsp";
        }
        if(r1_Code.equals("1")) {//代表支付成功,
            orderService.updateStatusByOid(r6_Order, 2);
            if(r9_BType.equals("1")) {//重定向
                req.setAttribute("code", "success");
                req.setAttribute("msg", "恭喜，支付成功！");
                return "f:/jsps/msg.jsp";
            } else if(r9_BType.equals("2")) {
                resp.getWriter().print("success");
            }
        }
        return null;
    }

    /**
     * 准备支付
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String preparePay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = orderService.getOrderByOid(req.getParameter("oid"));
        req.setAttribute("order",order);
        return "f:/jsps/order/pay.jsp";
    }
    /**
     * 取消订单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
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
        return "f:/jsps/msg.jsp";
    }

    /**
     * 确认收货
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String confirmOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        int status = orderService.getStatusByOid(oid);
        if (status != 3) {
            req.setAttribute("code", "error");
            req.setAttribute("msg", "无法确认收货");
        } else {
            orderService.updateStatusByOid(oid, 5);
            req.setAttribute("code", "success");
            req.setAttribute("msg", "订单交易成功");
        }
        return "f:/jsps/msg.jsp";
    }
    /**
     * 获取订单详细信息
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        String btn = req.getParameter("btn");
        Order order = orderService.getOrderByOid(oid);
        req.setAttribute("order",order);
        req.setAttribute("btn",btn);
        return "f:/jsps/order/desc.jsp";
    }
    /**
     * 创建表单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String creatOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address= req.getParameter("address");
        String cartItemIds = req.getParameter("cartItemIds");
        User user = (User) req.getSession().getAttribute("sessionUser");
        Order order = new Order();
        order.setAddress(address);
        order.setUser(user);
        orderService.add(order,cartItemIds);
        req.setAttribute("order",order);
        return "f:/jsps/order/ordersucc.jsp";
    }
    /**
     * 查询个人订单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getOrdersByUid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int ps = 8;//pagesize 这次固定了,懒得写了
        int pc = getPc(req)  ;
        User user = (User) req.getSession().getAttribute("sessionUser");
        String url = getUrl(req);
        Pager<Order> pager = orderService.getPagerByUid(user.getUid(),pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/jsps/order/list.jsp";
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
