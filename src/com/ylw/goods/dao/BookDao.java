package com.ylw.goods.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.ylw.goods.po.Book;
import com.ylw.goods.po.Category;
import com.ylw.goods.util.page.Expression;
import com.ylw.goods.util.page.Pager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 85243 on 2017/3/29.
 */
public class BookDao {
    QueryRunner qr = new TxQueryRunner();
    Logger log = Logger.getLogger(BookDao.class);

    public void delete(String bid) throws SQLException {
        String sql = "delete from t_book where bid = ?";
        qr.update(sql,bid);
    }

    /**
     * 修改图书
     * @param book
     *
     * @throws SQLException
     */
    public void edit(Book book) throws SQLException {
        String sql = "update t_book set bname=?,author=?,price=?,currPrice=?," +
                "discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?," +
                "printtime=?,booksize=?,paper=?,cid=? where bid=?";
        Object[] params = {book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(),
                book.getCategory().getCid(),book.getBid()};
        qr.update(sql, params);
    }

    /**
     * 添加图书
     * @param book
     * @throws SQLException
     */
    public void add(Book book) throws SQLException {
        String sql = "insert into t_book(bid,bname,author,price,currPrice," +
                "discount,press,publishtime,edition,pageNum,wordNum,printtime," +
                "booksize,paper,cid,image_w,image_b)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
                book.getImage_w(),book.getImage_b()};
        qr.update(sql, params);
    }
    /**
     * 查询该二级分类下的书的个数
     * @param cid
     * @return
     */
    public int countBookByCid(String cid) throws SQLException {
        String sql = "select count(*) from t_book where cid = ?";
        Number count = (Number) qr.query(sql,new ScalarHandler(),cid);
        return count.intValue();
    }

    /**
     * 通过bid查询书
     * 其实这个也可以命名成load的,hibernate里面加载一个本类对象的话,是用load或get的
     * @param id
     * @return
     */
    public Book getBookByBid(String id) throws SQLException {
        String sql = "select * from t_book where bid= ?";
        Map<String,Object> map = qr.query(sql, new MapHandler(),id);
        Book book = CommonUtils.toBean(map,Book.class);
        //这里也罢分类加进去,为了日后方便
        Category category = CommonUtils.toBean(map, Category.class);
        book.setCategory(category);
        return book;
    }

    /**
     * 多条件组合查询
     * @param book
     * @param pc
     * @param ps
     * @return
     */
    public Pager<Book> getPagerByCombination(Book book,int pc, int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname"," like ","%"+book.getBname()+"%"));
        expressions.add(new Expression("author"," like ","%"+book.getAuthor()+"%"));
        expressions.add(new Expression("press"," like ","%"+book.getPress()+"%"));
        return getPagerByCriteria(expressions,pc,ps);
    }
    /**
     * 通过出版社查找分页对象
     * @param press
     * @param pc
     * @param ps
     * @return
     * @throws SQLException
     */
    public Pager<Book> getPagerByPress(String press ,int pc, int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        log.info("press is : *"+press+"*");
        expressions.add(new Expression("press" ," like ","%"+press+"%"));//这里需要注意了百分号前面不能有空格,要不然查询语句是' %清华大学出版社%',多了一个空格,就是没有数据的
        return getPagerByCriteria(expressions,pc,ps);
    }

    /**
     * 通过书名查找分页对象
     * @param name
     * @param pc
     * @param ps
     * @return
     * @throws SQLException
     */
    public Pager<Book> getPagerByName(String name,int pc,int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("bname"," like ","%"+name+"%"));
        return getPagerByCriteria(expressions,pc,ps);
    }

    /**
     * 通过作者获取pager对象
     * @param author
     * @param pc
     * @param ps
     * @return
     * @throws SQLException
     */
    public Pager<Book> getPagerByAuthor(String author,int pc,int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("author"," like ","%"+author+"%"));
        return getPagerByCriteria(expressions,pc,ps);
    }
    /**
     * 通过分类获取pager对象
     * @param cid
     * @param pc
     * @param ps
     * @return
     * @throws SQLException
     */
   public Pager<Book> getPagerByCid(String cid,int pc,int ps) throws SQLException {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(new Expression("cid","=",cid));
        return getPagerByCriteria(expressions,pc,ps);
    }

    /**
     * 通用的查询方法
     * hibernate里面有类似方法
     * @param expressionList
     * @param pc             当前页码
     * @param ps             每页数据大小,pagesize
     * @return
     */
    private Pager<Book> getPagerByCriteria(List<Expression> expressionList, int pc, int ps) throws SQLException {
        String sql = "select count(*) from t_book";
        List<Object> params = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();
        //确定分页数据范围需要三个参数,tr,ps,pc  这里需要获取tr就好了
        int tr;
        sb.append(" where 1=1"); //为了方便后面添加 and a='1'   and b= '2' 因为第一个肯定要where,但是1=1不影响结果
        //把所有的查询参数获取出来,转化为sql语句
        if (expressionList != null&&expressionList.size()!=0) {
            for (Expression expression : expressionList) {
                sb.append(" and").append(" ").append(expression.getName()).append(" ").
                        append(expression.getOperator());
                if (!expression.getOperator().equals("is null")) { //因为is null 方法后面没有值,所以不用添加值
                    sb.append(" ").append("?");
                    params.add(expression.getValue());
                }
            }
        }
        // 1.获取tr 总数据数
        sql = sql + sb.toString();
        log.info(sql);
        Number trNum = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
        tr = trNum.intValue();
        log.info(tr);
//      2.过去分页中的对象 即listBean
        sql = "select * from t_book" + sb.toString() + " order by orderBy limit ?,?";//limit后面两个参数,分别为结果中的第几条,和选择几条结果(pagesize)
        params.add((pc - 1) * ps);
        params.add(ps);
        List<Book> bookList = qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());//这里需要注意的是,
        // 传进去的是数组,如果直接传list会报错,因为list传进去的是[1, 2] List对象,而数组传进去的是对象地址
//        设置Pager对象
        Pager<Book> pager = new Pager<>();
        pager.setPc(pc);
        pager.setPs(ps);
        pager.setTr(tr);
        pager.setListBean(bookList);
        return pager;
    }

    public static void main(String[] args) throws SQLException {
        List list = new ArrayList<>();
        list.add("1");
        list.add("2");
        String[] a ={"a","b"};
        System.out.println(list);
        System.out.println(list.toArray());
        System.out.println(a.toString());
        BookDao bd = new BookDao();
        Pager pager = bd.getPagerByCriteria(new ArrayList<Expression>() , 1, 4);
        System.out.println(pager.getListBean().size());
    }
}
