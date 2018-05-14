package cn.edu.nju.software.controller.mr;

import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/mr")
public class MRPageController {

    @RequestMapping(method = RequestMethod.GET)
    public String toMR(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mr/home";
    }

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public String toVideo(@PathVariable("id") String id,
                          Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "mr/video";
    }
}
