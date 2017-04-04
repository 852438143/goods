package com.ylw.goods.service.test;

import cn.itcast.commons.CommonUtils;
import com.ylw.goods.service.UserService;
import com.ylw.goods.service.exception.UserServiceException;
import com.ylw.goods.po.User;
import org.junit.Test;

/**
 * Created by 85243 on 2017/3/27.
 */
public class TestUserService {
    @Test
    public void test1(){
        UserService userService = new UserService();
        User user = new User();
        user.setUid(CommonUtils.uuid());
        user.setLoginname("shagou");
        user.setEmail("852438143@qq.com");
        userService.regist(user);
    }
    @Test
    public void test2() throws UserServiceException {
        UserService userService = new UserService();
        userService.updateLoginpass("x","123","111");
    }
}

