package com.ylw.goods.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.ylw.goods.po.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 85243 on 2017/3/29.
 */
public class CategoryDao {
    QueryRunner qr = new TxQueryRunner();
    Logger log = Logger.getLogger(CategoryDao.class);

    /**
     * 获取category
     * @param cid
     * @return
     * @throws SQLException
     */
    public Category get(String cid ) throws SQLException {
        String sql = "select * from t_category where cid =?";
        Map<String,Object> map = qr.query(sql,new MapHandler(),cid);
        Category category = CommonUtils.toBean(map,Category.class);
        if(map.get("pid")!=null){
            Category parent = new Category();
            parent.setCid((String) map.get("pid"));
            category.setParent(parent);
        }
        return category;
    }
    public int countChildsByParent(String pid) throws SQLException {
        String sql = "select count(*) from t_category where pid =?";
        Number result = (Number) qr.query(sql,new ScalarHandler(),pid);
        return result.intValue();
    }
    public void delete(String cid ) throws SQLException {
        String sql = "delete from t_category where cid =?";
        qr.update(sql,cid);
    }
    public void edit(Category category) throws SQLException {
        String sql = "update t_category set cname=?  , pid=? , `desc`=? where cid =?";
        String pid = null;
        if(category.getParent()!=null){
            pid = category.getParent().getCid();
        }
        System.out.println("params "+category.getCname()+"  "+pid+"  " +category.getDesc()+"  " +category.getCid());
        qr.update(sql,category.getCname(),pid,category.getDesc(),category.getCid());
    }

    /**
     * 查找所有的父分类
     * @return
     * @throws SQLException
     */
    public List<Category> getAllParents() throws SQLException {
        String sql = "select * from t_category where pid is null order by orderBy";//这里是is null 不是=null
        List<Category> categorys = qr.query(sql,new BeanListHandler<Category>(Category.class));
        log.info(categorys.size());
        return categorys;
    }

    /**
     * 添加一个分类
     * @param category
     */
    public void add(Category category) throws SQLException {
        String sql = "insert into t_category (cid,cname,pid,`desc`) values(?,?,?,?)";//关键字需要用`括起来;
        String pid = null;
        if(category.getParent()!=null){
            pid= category.getParent().getCid();
        }
        qr.update(sql,category.getCid(),category.getCname(),pid,category.getDesc());
    }
    /**
     * 查询出所有的分类
     * @return
     * @throws SQLException
     */
    public List<Category> findAll() throws SQLException {

        //查询出所有的父分类
        String sql = "select * from t_category where pid is null order by orderBy";
        List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());//其实这个就是父分类
        log.info("mapList size is: "+mapList.size());
        List<Category> parents  =  toCategoryList(mapList);

        //把所有的父分类所包含的子分类加到父分类中去
        for(Category parent : parents){
            List children = findByParent(parent);
            parent.setChildren(children);
        }
        return parents;
    }

    /**
     * 获取当前父分类的所有子分类
     * @param parent
     * @return
     * @throws SQLException
     */
    public List<Category> findByParent(Category parent) throws SQLException {
        String sql ="select * from t_category where pid = ?";
        String pid = parent.getCid();
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),pid);
        return toCategoryList(mapList);
    }

    /**
     * 把map对象转化为Category类对象 因为只能被内部的方法调用,所以采用私有的
     * @param map
     * @return
     */
    private Category toCategory(Map<String,Object> map){
        /*
		 * map {cid:xx, cname:xx, pid:xx, desc:xx, orderBy:xx}
		 * Category{cid:xx, cname:xx, parent:(cid=pid), desc:xx}
		 */
        Category category = CommonUtils.toBean(map,Category.class);//其实这样已经算把没有pid的封装到了Category里面了,
                                                                // 但是我们最后要的是二级目录,所以需要把第二级目录也封装到Category里面的children里面去
        String pid = (String) map.get("pid");
        if(pid!=null){//当pid不等于空的话,那么它肯定是子分类,有父分类,我们需要给这个对象加一个父分类
            Category parent = new Category();
            parent.setCid(pid);//设置一个父分类,
            category.setParent(parent);//把父分类加到子分类中,其实也是相当于把pid加进去了,
        }
        return category;
    }

    private List<Category> toCategoryList(List<Map<String,Object>> mapList){
        List<Category> list = new ArrayList();
        for(Map<String,Object> map : mapList){
            Category category = toCategory(map);
            list.add(category);
        }
        return list;
    }
}
