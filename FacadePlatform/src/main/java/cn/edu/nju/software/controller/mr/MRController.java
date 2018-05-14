package cn.edu.nju.software.controller.mr;

import cn.edu.nju.software.service.mr.MRService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/mr")
public class MRController {

    @Resource
    private MRService mrService;


}
