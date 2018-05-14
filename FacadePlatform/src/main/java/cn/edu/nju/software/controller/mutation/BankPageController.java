package cn.edu.nju.software.controller.mutation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/mutation/bank")
public class BankPageController {

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String toBankNew() {

        return "mutation/teacher/bank";
    }
}
