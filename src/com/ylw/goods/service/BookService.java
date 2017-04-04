package com.ylw.goods.service;

import com.ylw.goods.dao.BookDao;
import com.ylw.goods.po.Book;
import com.ylw.goods.util.page.Pager;

import java.sql.SQLException;

/**
 * Created by 85243 on 2017/3/29.
 */
public class BookService {
    BookDao bookDao  = new BookDao();

    public void delete(String bid){
        try {
            bookDao.delete(bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void edit(Book book){
        try {
            bookDao.edit(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加图书
     * @param book
     */
    public void add(Book book) {
        try {
            bookDao.add(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countBookByCid(String cid){
        try {
            return bookDao.countBookByCid(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过bid查询书
     * 其实这个也可以命名成load的,hibernate里面加载一个本类对象的话,是用load或get的
     * @param bid
     * @return
     */
    public Book getBookByBid(String bid){
        try {
            return bookDao.getBookByBid(bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pager<Book> getPagerByPress(String press , int pc, int ps)  {
        try {
            return bookDao.getPagerByPress(press,pc,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Pager<Book> getPagerByCategory(String cid,int pc,int ps)  {
        try {
            return bookDao.getPagerByCid(cid,pc,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Pager<Book> getPagerByName(String name, int pc, int ps) {
        try {
            return bookDao.getPagerByName(name,pc,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Pager<Book> getPagerByAuthor(String author, int pc, int ps) {
        try {
            return bookDao.getPagerByAuthor(author,pc,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Pager<Book> getPagerByCombination(Book book, int pc, int ps) {
        try {
            return bookDao.getPagerByCombination(book,pc,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
