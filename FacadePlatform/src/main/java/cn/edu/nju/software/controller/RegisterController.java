package cn.edu.nju.software.controller;

import cn.edu.nju.software.command.RegisterCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@RestController
public class RegisterController {
    @Autowired
    AccountService accountService;

    @PostMapping("/register")
    public Result register(@RequestBody RegisterCommand registerCommand) {
        return accountService.register(registerCommand);
    }
}
