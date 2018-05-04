package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.entity.Bank;
import cn.edu.nju.software.service.mutation.ExamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/mutation/exam")
public class ExamPageController {

    @Resource
    private ExamService examService;

    @RequestMapping(method = RequestMethod.GET)
    public String toExam(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mutation/"
                + (user == null || user.getRoleName() == null ? "teacher" : user.getRoleName())
                + "/exam";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String toExamNew(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);

        List<BankDto> banks = new ArrayList<>();

        model.addAttribute("banks", banks);
        return "mutation/teacher/exam-edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String toExamEdit(@PathVariable("id") Long id,
                             Model model) {
        ShiroUser user = ShiroUtils.currentUser();


        model.addAttribute("user", user);
        return "mutation/teacher/exam-edit";
    }
}
