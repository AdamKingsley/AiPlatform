package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.ExamCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.mutation.ExamService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mengf on 2018/4/18 0018.
 */
@RestController
@RequestMapping("exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("create")
    public Result add(@RequestBody ExamCommand command) {
        examService.create(command);
        return Result.success().message("创建考试成功！");
    }

    @PutMapping("update")
    public Result update(@RequestBody ExamCommand command) {
        examService.update(command);
        return Result.success().message("更新考试信息成功!");
    }

    @DeleteMapping("delete")
    public Result delete(@RequestParam("id") List<Long> ids) {
        examService.delete(ids);
        return Result.success().message("删除考试信息成功！");
    }

    @GetMapping("list")
    public PageResult list(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return examService.list(pageNum, pageSize);
    }

}
