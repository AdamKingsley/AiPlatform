package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/mutation/exam")
public class ExamPageController {

    @RequestMapping(method = RequestMethod.GET)
    public String toExam(Model model) {

        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mutation/"
                + (user == null || user.getRoleName() == null ? "student" : user.getRoleName())
                + "/exam";
    }
}
