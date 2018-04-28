package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.ModelCommand;
import cn.edu.nju.software.command.mutation.ModelPaginationCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.mutation.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@RestController
@RequestMapping("model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @GetMapping("list/{bankId}")
    public PageResult list(@PathVariable Long bankId,
                           @RequestParam(value = "pageNum",required = false)Integer pageNum,
                           @RequestParam(value = "pageSize",required = false)Integer pageSize,
                           @RequestParam(value = "type",required = false)Integer type,
                           @RequestParam(value = "startTime",required = false)Date startTime,
                           @RequestParam(value = "endTime",required = false)Date endTime) {
        ModelPaginationCommand command = new ModelPaginationCommand(pageNum,pageSize);
        command.setType(type);
        command.setStartTime(startTime);
        command.setEndTime(endTime);
        command.setBankId(bankId);
        return modelService.list(command);
    }

    /**
     * 更新某模型信息 如名称
     *
     * @param command
     * @return
     */
    @PutMapping("update")
    public Result update(@RequestBody ModelCommand command) {
        modelService.update(command);
        return Result.success().message("更新考试信息成功!");
    }

    /**
     * 删除模型
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delete")
    public Result delete(@RequestParam("id") List<Long> ids) {
        modelService.delete(ids);
        return Result.success().message("删除模型信息成功！");
    }

    @GetMapping("download/{id}")
    public void download(@PathVariable("id")Long id, HttpServletRequest request, HttpServletResponse response){
        modelService.download(id,response);
    }

}
