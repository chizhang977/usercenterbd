package com.justin.usercenterbd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.justin.usercenterbd.common.BusinessException;
import com.justin.usercenterbd.common.ErrorCode;
import com.justin.usercenterbd.mapper.UserMapper;

import com.justin.usercenterbd.model.domain.User;
import com.justin.usercenterbd.service.UserService;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.justin.usercenterbd.constant.UserConStant.USER_LOGIN_STATE;

/**
 * @author justin
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-03-30 18:24:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    @Resource
    private UserMapper userMapper;



    // 账号验证
    private static final String ACCOUNT_PATTERN = "^[a-zA-Z0-9_-]+$";
    private static final Pattern pattern = Pattern.compile(ACCOUNT_PATTERN);

    //性能优化，缓存Argon2
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    /**
     * 登录
     *[todo] 虽然用户信息进行了脱敏的处理，但是给前端暴漏了用户表的字段，可以封装vo
     * @param userAccount
     * @param userPassword
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不能小于8位");
        }
        // 账户不能包含特殊字符
        if (!pattern.matcher(userAccount).matches()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能包含特殊字符");
        }
        // 2、校验密码
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("userAccount", userAccount));
        boolean isPasswordValid = ARGON2.verify(user.getUserPassword(), userPassword.toCharArray());
        if (!isPasswordValid) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }

        // 3、用户信息脱敏
        User saftyUser = this.getSaftyUser(user);

        // 4、保存用户状态
        request.getSession().setAttribute(USER_LOGIN_STATE, saftyUser);
        System.out.println(request.getSession().getAttribute(USER_LOGIN_STATE));

        // 5、返回用户信息

        return saftyUser;
    }
    /**
     * 注册
     * @author justin
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        // 1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword,checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不能小于8位");
        }
        // 账户不能包含特殊字符
        if (!pattern.matcher(userAccount).matches()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能包含特殊字符");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }
        if (planetCode.length()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编号不能大于5位");
        }
        // 账号不能重复
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("userAccount",userAccount));

        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }

        //星球编号不能重复
        Long planetCodeCount = userMapper.selectCount(new QueryWrapper<User>().eq("planetCode",planetCode));

        if (planetCodeCount > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编号已存在");
        }
        // 2、加密
        String handledPassword = ARGON2.hash(3, 16384, 1, userPassword.toCharArray());

        // 3、插入信息
        User user = new User();
        user.setUserStatus(0);
        user.setUserPassword(handledPassword);
        user.setUserAccount(userAccount);
        user.setPlanetCode(planetCode);
        user.setUsername("user_" + RandomStringUtils.randomAlphanumeric(6));
        boolean saveResult = this.save(user);

        return saveResult ? user.getId() : -1;

    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    @Override
    public User getSaftyUser(User user) {
        if(user == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"脱敏数据为空");
        }
        User safeUser = new User();
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setEmail(user.getEmail());
        safeUser.setRole(user.getRole());
        safeUser.setPhone(user.getPhone());
        safeUser.setGender(user.getGender());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setUsername(user.getUsername());
        safeUser.setId(user.getId());
        safeUser.setPlanetCode(user.getPlanetCode());
        return safeUser;
    }


}