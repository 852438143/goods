package com.ylw.goods.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.ylw.goods.po.Book;
import com.ylw.goods.service.BookService;
import com.ylw.goods.util.page.Pager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 85243 on 2017/3/29.
 */
public class BookServlet extends BaseServlet{
    BookService bookService = new BookService();
    Logger log = Logger.getLogger(BookServlet.class);

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
        req.setAttribute("book",book);
        return "f:/jsps/book/desc.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
        return "f:/jsps/book/list.jsp";
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
