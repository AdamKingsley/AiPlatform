package cn.edu.nju.software.controller;

import cn.edu.nju.software.command.ChangePasswordCommand;
import cn.edu.nju.software.command.ResetPasswordCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mengf on 2018/4/10 0010.
 */

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/currentUser")
    public Result currentUser() {
        ShiroUser user = ShiroUtils.currentUser();
        if (user != null) {
            return Result.success().withData(user);
        } else {
            return Result.error().errorCode("-101").message("用户未登录或登录失效");
        }
    }

    @PostMapping("/change-password")
    public Result changePassword(@RequestBody ChangePasswordCommand command) {
        accountService.changePassword(command);
        return Result.success().message("修改密码成功！");
    }

    @PostMapping("/reset-password")
    public Result changePassword(@RequestBody ResetPasswordCommand command) {
        accountService.resetPassword(command);
        return Result.success().message("重置密码成功！");
    }

    @GetMapping("/active/{code}")
    public Result activeAccount(@PathVariable String code) {
        return accountService.active(code);
    }
}
