package cn.edu.nju.software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AutodrivePageController {

    @RequestMapping(value = "/autodrive")
    public String toAutodrive() {
        return "autodrive";
    }

}
