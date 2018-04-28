package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.ExamCommand;
import cn.edu.nju.software.command.mutation.ExamPageCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.mutation.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
     *
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
     *
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
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delete")
    public Result delete(@RequestParam("id") List<Long> ids) {
        examService.delete(ids);
        return Result.success().message("删除考试信息成功！");
    }

    @GetMapping("detail/{id}")
    public Result getExam(@PathVariable("id") Long id) {
        return examService.getExam(id);
    }

    /**
     * 获取考试列表
     * //TODO 筛选条件 同理如AccountController
     * //0还未开始；1正在进行；2已经结束
     * private Integer type;
     * //指全部的还是只是我参加过
     * private Boolean isMine;
     * //考试开始时间介于某个区间内
     * private Date startTime;
     * private Date endTime;
     */
    @GetMapping("list")
    public PageResult list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestParam(value = "type", required = false) Integer type,
                           @RequestParam(value = "isMine", required = false) Boolean isMine,
                           @RequestParam(value = "startTime", required = false) Date startTime,
                           @RequestParam(value = "endTime", required = false) Date endTime) {
        ExamPageCommand command = new ExamPageCommand(pageNum, pageSize);
        command.setType(type);
        command.setIsMine(isMine);
        command.setStartTime(startTime);
        command.setEndTime(endTime);
        command.setCurrentTime(new Date());
        return examService.list(command);
    }

}
