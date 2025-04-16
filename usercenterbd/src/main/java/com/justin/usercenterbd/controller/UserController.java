package com.justin.usercenterbd.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.justin.usercenterbd.common.BaseResponse;
import com.justin.usercenterbd.common.BusinessException;
import com.justin.usercenterbd.common.ErrorCode;
import com.justin.usercenterbd.common.ResultUitls;
import com.justin.usercenterbd.model.domain.User;
import com.justin.usercenterbd.model.request.RequestLogin;
import com.justin.usercenterbd.model.request.RequestRegister;
import com.justin.usercenterbd.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.justin.usercenterbd.constant.UserConStant.ADMIN_ROLE;
import static com.justin.usercenterbd.constant.UserConStant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注销方法
     * @param request
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (sessionUser == null) {
           throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        //确保用户数据是最新的
        User user = userService.getById(sessionUser.getId());
        User result = userService.getSaftyUser(user);
        return ResultUitls.success(result);
    }


    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestParam(required = false) Long id,HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUitls.success(result);
    }
    /**
     * 查询用户,需要鉴权，只允许管理员访问，也就是role为1的用户
     * @param username
     * @return
     */
    @GetMapping("search")
    public BaseResponse<List<User>> searchUser(String username,HttpServletRequest request) {
        //仅管理员可以查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        //查询出来的用户要脱敏
        List<User> list = userService.list(queryWrapper);
        List<User> result = list.stream().map(user -> userService.getSaftyUser(user)).collect(Collectors.toList());
        return ResultUitls.success(result);

    }

    /**
     * 用户查询
     * @param requestRegister
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody RequestRegister requestRegister){
        if (requestRegister == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = requestRegister.getUserAccount();
        String userPassword = requestRegister.getUserPassword();
        String checkPassword = requestRegister.getCheckPassword();
        String planetCode  = requestRegister.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUitls.success(result);
    }

    /**
     * 用户登录
     * @param requestLogin
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody RequestLogin requestLogin, HttpServletRequest request){
        String userAccount = requestLogin.getUserAccount();
        String userPassword = requestLogin.getUserPassword();

        if (userAccount == null && userPassword == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUitls.success(result);
    }

    /**
     * 鉴权方法提取
     * @param request
     * @return
     */
    private  boolean isAdmin(HttpServletRequest request){
        //仅管理员可以查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getRole() == ADMIN_ROLE;
    }


}
