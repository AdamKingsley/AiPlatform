package cn.edu.nju.software.controller;

import cn.edu.nju.software.command.ChangePasswordCommand;
import cn.edu.nju.software.command.ResetPasswordCommand;
import cn.edu.nju.software.command.UserPaginationCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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

    @PostMapping(value = "/change-password")
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


    //todo 传参很多很蠢 GET不支持requestbody 寻求解决方案
    @GetMapping("/users")
    public PageResult list(@RequestParam(value = "pageNum", required = false) Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestParam(value = "draw", required = false) Integer draw,
                           @RequestParam(value = "startCreateTime", required = false) Date startCreateTime,
                           @RequestParam(value = "endCreateTime", required = false) Date endCreateTime,
                           @RequestParam(value = "startModifyTime", required = false) Date startModifyTime,
                           @RequestParam(value = "endModifyTime", required = false) Date endModifyTime,
                           @RequestParam(value = "state", required = false) Integer state, HttpServletRequest request) {
        UserPaginationCommand command = new UserPaginationCommand(pageNumber, pageSize,draw);
        command.setState(state);
        command.setStartCreateTime(startCreateTime);
        command.setEndCreateTime(endCreateTime);
        command.setStartModifyTime(startModifyTime);
        command.setEndModifyTime(endModifyTime);
        return accountService.list(command);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteAccounts(@RequestParam("id") List<Long> ids) {
        return accountService.delete(ids);
    }

    /**
     * 冻结用户
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/blocked")
    public Result blockAccounts(@RequestParam("id") List<Long> ids) {
        return accountService.block(ids);
    }


    @PostMapping("/reactive")
    public Result reactive(@RequestParam("id") List<Long> ids) {
        return accountService.reactive(ids);
    }
}
