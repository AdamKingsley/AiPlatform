package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.ExamCommand;
import cn.edu.nju.software.command.mutation.ExamPageCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.mutation.ExamService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

/**
 * Created by mengf on 2018/4/18 0018.
 */
@RestController
@RequestMapping("exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    /**
     * 创建考试
     * @param command
     * @return
     */
    @PostMapping("create")
    public Result add(@RequestBody ExamCommand command) {
        examService.create(command);
        return Result.success().message("创建考试成功！");
    }

    /**
     * 更新考试信息(仅当考试未结束的时候)
     * @param command
     * @return
     */
    @PutMapping("update")
    public Result update(@RequestBody ExamCommand command) {
        examService.update(command);
        return Result.success().message("更新考试信息成功!");
    }

    /**
     * 删除考试(管理员)
     * @param ids
     * @return
     */
    @DeleteMapping("delete")
    public Result delete(@RequestParam("id") List<Long> ids) {
        examService.delete(ids);
        return Result.success().message("删除考试信息成功！");
    }

    /**
     * 获取考试列表
     * //TODO 筛选条件
     * @param command
     * @return
     */
    @GetMapping("list")
    public PageResult list(@RequestBody ExamPageCommand command) {
        return examService.list(command);
    }

}
