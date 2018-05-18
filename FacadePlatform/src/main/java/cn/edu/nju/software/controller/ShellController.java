package cn.edu.nju.software.controller;

import cn.edu.nju.software.command.shell.ProcessModelCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.service.shell.ProcessModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：
 * 创建者：Zeros
 * 创建时间：2018/5/16 下午4:20
 * 包名：cn.edu.nju.software.controller
 */

@RestController
@RequestMapping("/shell")
public class ShellController {

    @Autowired
    private ProcessModelService service;

    @GetMapping("/process")
    public Result get() {
        ProcessModelCommand command = new ProcessModelCommand();
        command.setExamId(1L);
        command.setUserId(1L);
        command.setPath("/home/upload/exercise/11/5/1");
        List<ModelDto> modelDtos = new ArrayList<>();
        ModelDto modelDto = new ModelDto();
        modelDto.setLocation("/home/upload/bank_4/models/model_1.hdf5");
        modelDto.setStandardModelLocation("/home/upload/bank_4/standard_model/model.hdf5");
        modelDto.setScriptLocation("/home/upload/bank_4/script.py");
        modelDto.setId(Long.parseLong("1"));
        modelDtos.add(modelDto);
        command.setModels(modelDtos);
        return service.processModel(command);
    }
}
