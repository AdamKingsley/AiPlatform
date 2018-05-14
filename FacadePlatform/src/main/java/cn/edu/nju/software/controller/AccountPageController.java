package cn.edu.nju.software.controller;

import cn.edu.nju.software.common.shiro.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/account")
public class AccountPageController {

    @RequestMapping(value = "/change-info", method = RequestMethod.GET)
    public String toChangePassword(Model model) {
        model.addAttribute("user", ShiroUtils.currentUser());
        return "user/change-info";
    }
}
