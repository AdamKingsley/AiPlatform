package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.service.mutation.BankService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/mutation/bank")
public class BankPageController {

    @Resource
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public String toBank(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mutation/teacher/bank";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String toBankNew(Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        model.addAttribute("user", user);
        return "mutation/teacher/bank-new";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String toBankEdit(@PathVariable("id") Long id,
                             Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        BankDto bankDto = bankService.getBankDetail(id);

        model.addAttribute("user", user);
        model.addAttribute("bank", bankDto);
        return "mutation/teacher/bank-edit";
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public String toBankInfo(@PathVariable("id") Long id,
                             Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        BankDto bankDto = bankService.getBankDetail(id);

        model.addAttribute("user", user);
        model.addAttribute("bank", bankDto);
        return "mutation/student/bank-info";
    }
}
