package com.ylw.goods.admin.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.ylw.goods.admin.po.Admin;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * Created by 85243 on 2017/4/2.
 */
public class AdminDao {
    QueryRunner qr = new TxQueryRunner();

    public Admin getAdmin(String adminName,String adminPwd) throws SQLException {
        String sql = "select * from t_admin where adminname =? and adminpwd =?";
        Admin admin = qr.query(sql,new BeanHandler<Admin>(Admin.class),adminName,adminPwd);
        return admin;
    }
}
