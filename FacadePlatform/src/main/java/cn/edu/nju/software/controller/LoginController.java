package cn.edu.nju.software.controller;

import cn.edu.nju.software.command.LoginCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.entity.User;
import cn.edu.nju.software.service.AccountService;
import cn.edu.nju.software.util.DigestsUtil;
import cn.edu.nju.software.util.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by mengf on 2018/4/9 0009.
 */
@RestController
public class LoginController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = "application/json")
    public Result login(@RequestBody LoginCommand command) {
        if (Validate.checkMail(command.getUsername())) {
            //如果是邮箱登录需要去数据库里面拿到对应的用户名
            UserDto user = accountService.findByMailAddress(command.getUsername());
            command.setUsername(user.getUsername());
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(command.getUsername(), command.getPassword());
        token.setRememberMe(command.getRememberMe());
        subject.login(token);
        return Result.success().message("登录成功").withData(ShiroUtils.currentUser());
    }

    @PostMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.success().message("退出登录成功");
    }

    // 测试 后 删除
//    @GetMapping("/loginGet")
//    public Result loginGet(@RequestParam String username, @RequestParam String password) {
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        subject.login(token);
//
//        return Result.success().message("登录成功").withData(ShiroUtils.currentUser());
//
//
//    }
//
//    @RequestMapping(value = "/loginWithMail", method = RequestMethod.POST)
//    public Result loginWithMail(@RequestParam String mailAddress, @RequestParam String password) {
//        return Result.error().message("暂不支持邮箱登录");
//    }
}
