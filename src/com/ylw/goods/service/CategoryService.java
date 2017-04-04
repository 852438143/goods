package com.ylw.goods.service;

import com.ylw.goods.dao.CategoryDao;
import com.ylw.goods.po.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 85243 on 2017/3/29.
 */
public class CategoryService {
    CategoryDao categoryDao = new CategoryDao();


    public List<Category> findByParent(String pid){
        Category parent =  new Category();
        parent.setCid(pid);
        try {
            return  categoryDao.findByParent(parent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category get(String cid){
        try {
            return categoryDao.get(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int countChildsByParent(String cid){
        try {
            return categoryDao.countChildsByParent(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public  void delete(String cid ){
        try {
            categoryDao.delete(cid);
        } catch (SQLException e) {
            throw new  RuntimeException(e);
        }
    }
    public void edit(Category category){
        try {
            categoryDao.edit(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Category category){
        try {
            categoryDao.add(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getAllParents(){
        try {
            return categoryDao.getAllParents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Category>  findAll(){
        try {
            return categoryDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
