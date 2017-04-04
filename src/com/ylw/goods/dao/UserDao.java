package com.ylw.goods.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.ylw.goods.po.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * 用户模块持久层
 * Created by 85243 on 2017/3/25.
 */
public class UserDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * 通过uid和loginpass获取密码
     * @param uid
     * @param loginpass
     * @return
     */
    public boolean findUserByUidAndLoginPass(String uid,String loginpass){
        String sql = "select count(*) from t_user where uid =? and loginpass = ?";
        try {
            Number number = (Number)qr.query(sql,new ScalarHandler(),uid,loginpass);
            return number.intValue()>0;
        } catch (SQLException e) {
            //这里处理过了就不会向上面抛出sql异常了
        }
        return false;
    }

    public void updateUserByUidAndLoginpass(String uid ,String loginpass) throws SQLException {
        String sql = "update t_user set loginpass =? where uid=?";
        qr.update(sql,loginpass,uid);
    }

    /**
     * 通过用户名和密码获取User
     * @param loginname
     * @param loginpass
     * @return
     */
    public User getUserByLoginnameAndPassword(String loginname,String loginpass) throws SQLException {
        String sql = "select * from t_user where loginname=? and loginpass = ?";
        return qr.query(sql,new BeanHandler<User>(User.class),loginname,loginpass);
    }

    /**
     * 通过激活吗获取user对象
     * @param activationCode
     */
    public User getUserByActivationCode(String activationCode){
        String sql = "select * from t_user where activationCode =?";
        try {
            return qr.query(sql,new  BeanHandler<User>(User.class),activationCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过uid更新user是否激活的状态
     * @param uid
     * @param status
     * @throws SQLException
     */
    public void updateStatusByUid(String uid ,Boolean status) throws SQLException {
        String sql = "update t_user set status =? where uid=?";
        qr.update(sql,status,uid);
    }

    /**
     * 注册
     */
    public int  add(User user){
        String sql  ="insert into t_user VALUE(?,?,?,?,?,?)";
        QueryRunner qr = new TxQueryRunner();
        Object[] params ={user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.isStatus(),user.getActivationCode()};
        int result = 0;
        try {
            result = qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);//异常的话,如果不处理就往上面抛出,给上面处理
        }
        return result;
    }
    /**
     * 验证名字是否被注册
     * @param loginname
     * @return
     * @throws SQLException
     */
    public boolean ajaxValidateLoginname(String loginname) throws SQLException {
        System.out.println("Dao loginname is "+loginname);
        String sql = "select count(1) from t_user where loginname=?";
        Number number = (Number)qr.query(sql, new ScalarHandler(),loginname);
        return number.intValue()==0;
    }

    /**
     * 验证邮箱是否被注册
     * @param email
     * @return
     * @throws SQLException
     */
    public boolean ajaxValidateEmail(String email) throws SQLException {
        System.out.println("Dao email is "+email);
        String sql = "select count(1) from t_user where email=?";
        Number number = (Number)qr.query(sql, new ScalarHandler(),email);
        return number.intValue()==0;
    }
}
