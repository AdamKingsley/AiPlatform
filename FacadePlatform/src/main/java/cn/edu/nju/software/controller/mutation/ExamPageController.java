package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.dto.ExamDto;
import cn.edu.nju.software.dto.ExamResultDto;
import cn.edu.nju.software.service.mutation.BankService;
import cn.edu.nju.software.service.mutation.ExamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/mutation")
public class ExamPageController {

    @Resource
    private ExamService examService;
    @Resource
    private BankService bankService;

    @RequestMapping(value = "/t/exam", method = RequestMethod.GET)
    public String toExam(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mutation/teacher/exam";
    }

    @RequestMapping(value = "/s/exam", method = RequestMethod.GET)
    public String toExamStudent(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mutation/student/exam";
    }

    @RequestMapping(value = "/exam/new", method = RequestMethod.GET)
    public String toExamNew(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);

        List<BankDto> banks = bankService.listAll();

        model.addAttribute("banks", banks);
        return "mutation/teacher/exam-edit";
    }

    @RequestMapping(value = "/exam/edit/{id}", method = RequestMethod.GET)
    public String toExamEdit(@PathVariable("id") Long id,
                             Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        ExamDto exam = examService.getExam(id);
        model.addAttribute("user", user);
        model.addAttribute("exam", exam);
        return "mutation/teacher/exam-edit";
    }

    @RequestMapping(value = "/exam/info/{id}", method = RequestMethod.GET)
    public String toExamInfo(@PathVariable("id") Long id,
                             Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        ExamDto exam = examService.getExam(id);
        ExamResultDto result = examService.getExamResult(id);
        model.addAttribute("user", user);
        model.addAttribute("exam", exam);
        model.addAttribute("result", result);
        return "mutation/teacher/exam-info";
    }

    @RequestMapping(value = "/exam/join/{id}", method = RequestMethod.GET)
    public String toExamJoin(@PathVariable("id") Long id,
                             Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        ExamDto exam = examService.getExam(id);

        model.addAttribute("exam", exam);
        model.addAttribute("user", user);
        return "mutation/student/join";
    }

//    @RequestMapping(value = "/exam/opt/{id}", method = RequestMethod.GET)
//    public String toExamOpt(@PathVariable("id") Long id,
//                            Model model) {
//        ShiroUser user = ShiroUtils.currentUser();
//
//        ExamDto exam = examService.getExam(id);
//
//        model.addAttribute("exam", exam);
//        model.addAttribute("user", user);
//        return "mutation/student/opt";
//    }


}
