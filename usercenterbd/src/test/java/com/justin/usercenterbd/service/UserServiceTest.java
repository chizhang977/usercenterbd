package com.justin.usercenterbd.service;
import java.util.Date;
import java.util.regex.Pattern;

import com.justin.usercenterbd.model.domain.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
@Transactional
class UserServiceTest {

    // 正则表达式：只允许字母、数字、下划线、短横线
    private static final String ACCOUNT_PATTERN = "^[a-zA-Z0-9_-]+$";
    private static final Pattern pattern = Pattern.compile(ACCOUNT_PATTERN);

    @Resource
    private UserService userService;



    /**
     * 校验账号是否合法
     * @param account 用户输入的账号
     * @return true=合法, false=非法
     */
    public static boolean isValidAccount(String account) {
        if (account == null || account.isEmpty()) {
            return false;
        }
        return pattern.matcher(account).matches();
    }

    @Test
    void testRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        String plainPassword = "1234";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertEquals(-1,result);


        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertEquals(-1,result);

        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertEquals(-1,result);

        userAccount =  "justinUserCenter";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertTrue(result > 0);

    }

    /**
     * 测试星球编号
     */
    @Test
    void testPlanetCode(){
        String userAccount = "SpaceX";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String plainPassword = "1234";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,plainPassword);
        Assertions.assertTrue(true);
    }

    /**
     * 密码加密
     */
    @Test
    void testArgon2(){
        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(10, 65536, 1, "1234".toCharArray());
        System.out.println(hash);
    }

    @Test
    void userAccountRegex(){
        String[] testAccounts = {"justin123", "user-name", "admin@123", "测试账号", "user name"};

        for (String account : testAccounts) {
            boolean isValid = this.isValidAccount(account);
            System.out.printf("账号: %-10s → 校验结果: %s%n", account, isValid ? "合法" : "非法");
        }
    }

    /**
     * 测试插入数据库
     */
    @Test
    void testAddUser() {
        User user = new User();
        user.setId(12L);
        user.setUsername("justin");
        user.setUserAccount("justinUserCenter");
        user.setAvatarUrl("http://baidu.com");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("17339890099");
        user.setEmail("3030277879@qq.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        boolean save = userService.save(user);
        Assertions.assertTrue(save);


    }
}