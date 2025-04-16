package com.justin.usercenterbd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.justin.usercenterbd.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author justin
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-03-30 18:24:26
*/
public interface UserService extends IService<User> {



    /**
     * 登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户脱敏
     */
    User getSaftyUser(User user);

}
