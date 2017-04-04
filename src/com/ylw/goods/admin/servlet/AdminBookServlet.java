package com.ylw.goods.admin.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.Book;
import com.ylw.goods.po.Category;
import com.ylw.goods.service.BookService;
import com.ylw.goods.service.CategoryService;
import com.ylw.goods.util.page.Pager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 85243 on 2017/4/3.
 */
public class AdminBookServlet extends BaseServlet{
    private BookService bookService = new BookService();
    private CategoryService categoryService = new CategoryService();
    Logger log = Logger.getLogger(AdminBookServlet.class);


    public String delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bid = req.getParameter("bid");
        Book book = bookService.getBookByBid(bid);
        String realPath = this.getServletContext().getRealPath("/");
        new File(realPath,book.getImage_b()).delete();
        System.out.println(realPath+" "+book.getImage_b());
        new File(realPath,book.getImage_w()).delete();
        bookService.delete(bid);
        req.setAttribute("msg","删除成功");
        return "f:/adminjsps/msg.jsp";
    }

    public String edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map map = req.getParameterMap();
        Book book = CommonUtils.toBean(map,Book.class);
        Category category = CommonUtils.toBean(map,Category.class);
        book.setCategory(category);
        bookService.edit(book);
        System.out.println("------------------");
        req.setAttribute("msg","修改成功");
        return "f:/adminjsps/msg.jsp";
    }

    public String ajaxGetChildren(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        System.out.println(pid);
        List<Category> categories = categoryService.findByParent(pid);
        String result = toJson(categories);
        System.out.println(result);
        resp.getWriter().print(result);
        return null;
    }
    private String toJson(Category category){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"").append("cid").append("\"").append(":").append("\"").append(category.getCid()).append("\"");
        sb.append(",");
        sb.append("\"").append("cname").append("\"").append(":").append("\"").append(category.getCname()).append("\"");
        sb.append("}");
        return sb.toString();
    }
//    返回一个json数组,[{"cid":"value","cname":"value"},{}]
    private String toJson(List<Category> categorys){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i =0;i<categorys.size();i++){
            sb.append(toJson(categorys.get(i)));
            if(i<categorys.size()-1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    public String addBookPrepare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> parents =  categoryService.getAllParents();
        req.setAttribute("parents",parents);
        return "f:/adminjsps/admin/book/add.jsp";
    }
    /**
     * 通过bid查询书
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getBookByBid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bid = req.getParameter("bid");
        Book book = bookService.getBookByBid(bid);
        String cid = book.getCategory().getCid();
        Category child = categoryService.get(cid);
        String pid = child.getParent().getCid();
        List<Category> parents = categoryService.getAllParents();
        List<Category> children = categoryService.findByParent(pid);
        req.setAttribute("book",book);
        req.setAttribute("pid",pid);
        req.setAttribute("parents",parents);
        req.setAttribute("children",children);
        return "f:/adminjsps/admin/book/desc.jsp";
    }
    /**
     * 通过书名查书
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getPagerByName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取pc,和ps//这个项目中不能更该ps大小,不过随便写写啦
        int pc, ps;
        int[] a = getPcAndPs(req);
        pc = a[0];
        ps = a[1];
        //得到url
        String url = getUrl(req);
        //获取查询条件
        String bname = req.getParameter("bname");
        //查询分页
        Pager<Book> pager = bookService.getPagerByName(bname,pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     * 通过作者查询
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getPagerByAuthor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取pc,和ps//这个项目中不能更该ps大小,不过随便写写啦
        int pc, ps;
        int[] a = getPcAndPs(req);
        pc = a[0];
        ps = a[1];
        //得到url
        String url = getUrl(req);
        //获取查询条件
        String author = req.getParameter("author");
        //查询分页
        Pager<Book> pager = bookService.getPagerByAuthor(author,pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     * 通过出版社查询
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getPagerByPress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取pc,和ps//这个项目中不能更该ps大小,不过随便写写啦
        int pc, ps;
        int[] a = getPcAndPs(req);
        pc = a[0];
        ps = a[1];
        //得到url
        String url = getUrl(req);
        //获取查询条件
        req.setCharacterEncoding("utf-8");
        String press = req.getParameter("press");
        //查询分页
        Pager<Book> pager = bookService.getPagerByPress(press,pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/book/list.jsp";
    }
    public String getPagerByCombination(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取pc,和ps//这个项目中不能更该ps大小,不过随便写写啦
        int pc, ps;
        int[] a = getPcAndPs(req);
        pc = a[0];
        ps = a[1];
        //得到url
        String url = getUrl(req);
        //获取查询条件
        Book book = CommonUtils.toBean(req.getParameterMap(),Book.class);
        //查询分页
        Pager<Book> pager = bookService.getPagerByCombination(book,pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/book/list.jsp";
    }
    /**
     * 通过分类获取分页
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String getPagerByCatogory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取pc,和ps//这个项目中不能更该ps大小,不过随便写写啦
        int pc, ps;
        int[] a = getPcAndPs(req);
        pc = a[0];
        ps = a[1];
        //得到url
        String url = getUrl(req);
        //获取查询条件
        String cid = req.getParameter("cid");
        //查询分页
        Pager<Book> pager = bookService.getPagerByCategory(cid,pc,ps);
        pager.setUrl(url);
        req.setAttribute("pager",pager);
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     *截取url，页面中的分页导航中需要使用它做为超链接的目标！后期分页是以get方法传参数的所以需要url
     * @param req
     * @return
     */
    private String getUrl(HttpServletRequest req){
        	/*
	 * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pc=3
	 * /goods/BookServlet + methed=findByCategory&cid=xxx&pc=3
	 */
        String url = req.getRequestURI()+"?"+req.getQueryString();
        log.info(url);
        //截取掉pc参数
        int index = url.lastIndexOf("&pc=");
        if(index!=-1){
            url = url.substring(0,index);
        }
        log.info("after remove pc: "+url);
        return url;
    }

    /**
     * 获取pc和ps
     * @param req
     * @return
     */
    private int[] getPcAndPs(HttpServletRequest req){
        //默认的当前页和页大小 为1 和12
        int pc=1;
        int ps = 12;
        String pcStr = req.getParameter("pc");
        String psStr = req.getParameter("ps");
        if(pcStr!=null&&!pcStr.trim().equals("")){
            pc = Integer.parseInt(pcStr);
        }
        if(psStr!=null&&!psStr.trim().equals("")){
            ps = Integer.parseInt(psStr);
        }
        return new int[] {pc,ps};
    }
}
