package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.ProcessPaginationCommand;
import cn.edu.nju.software.command.shell.ProcessModelCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.ModelProcessDto;
import cn.edu.nju.software.entity.ModelProcess;
import cn.edu.nju.software.service.mutation.ModelProcessService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@RestController
@RequestMapping("model-process")
public class ModelProcessController {

    @Autowired
    private ModelProcessService service;

    @GetMapping("/iter")
    public Result getProcessIters(@RequestParam Long userId, @RequestParam Long examId, @RequestParam Long modelId) {
        Integer iters = service.getIters(userId, examId, modelId);
        return Result.success().message("获取该模型运行的次数成功！").withData(iters);
    }

    @GetMapping("/detail")
    public Result getProcessDetail(@RequestParam Long userId, @RequestParam Long examId, @RequestParam Long modelId, @RequestParam Integer iter) {
        ModelProcessDto dto  = service.getProcessDetail(userId,examId,modelId,iter);
        return Result.success().message("获取当前测试模型的详细测试信息成功！").withData(dto);
    }



//    @GetMapping("/list/all")
//    public Result getModelProcessList(@RequestParam Long userId, @RequestParam Long examId, @RequestParam Long modelId) {
//        List<ModelProcessDto> processDtos = service.getAll(userId, examId, modelId);
//        return Result.success().withData(processDtos).message("获取该模型的所有测试结果成功！");
//    }
//
//    @PostMapping("/list")
//    public PageResult getModelProcessList(@RequestParam Long userId, @RequestParam Long examId,
//                                          @RequestParam Long modelId, @RequestBody ProcessPaginationCommand command) {
//        return service.list(userId, examId, modelId, command);
//    }


}
