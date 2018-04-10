package cn.edu.nju.software.service;

import cn.edu.nju.software.entity.UserInfo;
import org.springframework.stereotype.Service;

/**
 * Created by mengf on 2018/4/9 0009.
 */
@Service
public class AccountService {

    public UserInfo findByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("username");
        userInfo.setPassword("123456");
        userInfo.setRoleType(1);
        userInfo.setState(1);
        userInfo.setRoleName("admin");
        userInfo.setSalt("hello");
        return userInfo;
    }}
