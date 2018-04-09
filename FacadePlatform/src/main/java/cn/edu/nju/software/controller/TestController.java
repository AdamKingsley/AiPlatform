package cn.edu.nju.software.controller;

import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.entity.TestEntity;
import cn.edu.nju.software.entity.UserFee;
import cn.edu.nju.software.mapper.UserMapper;
import cn.edu.nju.software.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mengf on 2018/4/2 0002.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService service;
    @Autowired
    private UserMapper mapper;

    @GetMapping("/one")
    public TestEntity get() {
        return service.get();
    }

    @GetMapping("/list")
    public List<TestEntity> getList() {
        return service.getList();
    }

    @GetMapping("/userFee/{id}")
    public UserFee getUserFee(@PathVariable("id") Long id) {
        return service.getUserFee(id);
    }

    @GetMapping("/error")
    public Result getError(){
        throw new ServiceException(ExceptionEnum.UNKNOW_ERROR);
    }

    @GetMapping("/userfee/one/{id}")
    public UserFee getUserFeeOne(@PathVariable("id") Long id) {
        return mapper.selectByPrimaryKey(id);
    }
}
