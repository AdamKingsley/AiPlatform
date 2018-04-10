package cn.edu.nju.software.controller;

import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;


/**
 * Created by mengf on 2018/4/9 0009.
 */
@RestController
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestParam String username, @RequestParam String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        subject.login(token);

        return Result.success().message("登录成功").withData(ShiroUtils.currentUser());


    }

    // 测试 后 删除
    @GetMapping("/loginGet")
    public Result loginGet(@RequestParam String username, @RequestParam String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        subject.login(token);

        return Result.success().message("登录成功").withData(ShiroUtils.currentUser());


    }

    @RequestMapping(value = "/loginWithMail", method = RequestMethod.POST)
    public Result loginWithMail(@RequestParam String mailAddress, @RequestParam String password) {
        return Result.error().message("暂不支持邮箱登录");
    }
}