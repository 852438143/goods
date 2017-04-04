package com.ylw.goods.admin.service;

import com.ylw.goods.admin.dao.AdminDao;
import com.ylw.goods.admin.po.Admin;

import java.sql.SQLException;

/**
 * Created by 85243 on 2017/4/2.
 */
public class AdminService {
    private AdminDao adminDao = new AdminDao();
    public Admin  getAdmin(String adminName,String adminPwd){
        try {
            return adminDao.getAdmin(adminName,adminPwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
