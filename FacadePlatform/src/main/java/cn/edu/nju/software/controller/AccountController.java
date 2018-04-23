package cn.edu.nju.software.controller;

import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mengf on 2018/4/10 0010.
 */

@RestController
public class AccountController {

    @GetMapping("/currentUser")
    public Result currentUser() {
        ShiroUser user = ShiroUtils.currentUser();
        if (user != null) {
            return Result.success().withData(user);
        } else {
            return Result.error().errorCode("-101").message("用户未登录或登录失效");
        }
    }

}
