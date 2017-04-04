package com.ylw.goods.service;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import com.ylw.goods.service.exception.UserServiceException;
import com.ylw.goods.dao.UserDao;
import com.ylw.goods.po.User;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 用户模块业务层
 * Created by 85243 on 2017/3/25.
 */
public class UserService {
    UserDao userDao = new UserDao();
    Logger logger = Logger.getLogger(UserService.class);


    public void updateLoginpass(String uid,String oldLoginpass,String newLoginpass) throws UserServiceException {
        if(!userDao.findUserByUidAndLoginPass(uid,oldLoginpass)){//因为findUserByUidAndLoginpass异常已经处理了,这里没有try-catch
            throw new UserServiceException("原始密码错误!!!");   //这句话写完后,需要把异常传到上面去,给调用者处理
        }
        try {
            userDao.updateUserByUidAndLoginpass(uid,newLoginpass);
        } catch (SQLException e) {
            throw new RuntimeException(e);//因为这个异常没有用,所以直接在这里处理了,不往上抛了
        }

    }


    /**
     * 处理登陆
     * @param user
     */
    public  User login(User user){
        try {
            return userDao.getUserByLoginnameAndPassword(user.getLoginname(),user.getLoginpass());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 激活邮箱
     * @param activationCode
     * @throws UserServiceException
     */
    public void activation(String activationCode) throws UserServiceException {
        User user = userDao.getUserByActivationCode(activationCode);
        logger.info(user);
        try {
            //这里选择抛出异常,这里有一个自定义的异常,其实也就是有几个构造方法,主要是为了抛出异常的时候给调用者捕获和处理
            if (user == null) throw new UserServiceException("注册码错误,该注册码没有对应的用户！！");
            if (user.isStatus() == true) {
                throw new UserServiceException("你已经激活过了,请勿重新激活");
            } else {
                userDao.updateStatusByUid(user.getUid(), true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    public int regist(User user) {
        int result = 0;
        user.setUid(CommonUtils.uuid());
        user.setStatus(false);
        user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
        //向数据库保存数据
        userDao.add(user);
//      向用户发送邮件
        //把配置文件加入到properties中
        Properties properties = new Properties();
        try {
            //因为字符编码问题,properties文件的字符编码为ios编码,我们需要转化为utf-8所以需要换种方法
//            properties.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
            URL resource = this.getClass().getClassLoader().getResource("email_template.properties");
            properties.load(new InputStreamReader(resource.openStream(), "utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //登陆邮件服务器,得到session
        String host = properties.getProperty("host");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        Session session = MailUtils.createSession(host, username, password);

        //创建mail对象
        String from = properties.getProperty("from");
        String to = user.getEmail();
        String subject = properties.getProperty("subject");
        String content = properties.getProperty("content");
        //MessageFromat.format("你好{0},我是{1}","傻狗","杨立伟"); 返回 你好傻狗,我是杨立伟,   相当于替换了里面的占位符{0} {1}
        content = MessageFormat.format(content, user.getActivationCode());
        Mail mail = new Mail(from, to, subject, content);
        try {
            MailUtils.send(session, mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过loginname查询是否存在loginname
     *
     * @param loginname
     * @return
     */
    public boolean ajaxValidateLoginname(String loginname) {
        try {
            return userDao.ajaxValidateLoginname(loginname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ajaxValidateEmail(String email) {
        try {
            return userDao.ajaxValidateEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
