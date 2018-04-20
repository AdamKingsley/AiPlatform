package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.BankCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.mutation.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@RestController
@RequestMapping("bank")
@Slf4j
public class BankController {
    @Autowired
    private BankService bankService;

    /**
     * 添加题库
     * @return Result
     */
    @PostMapping("create")
    public Result addBank(@RequestBody BankCommand command) {
        bankService.create(command);
        return Result.success().message("添加题库成功！");
    }

    /**
     * 更新题库基本信息
     *
     * @param id
     * @param command
     * @return
     */
    @PutMapping("update/{id}")
    public Result updateBank(@PathVariable Long id, @RequestBody BankCommand command) {
        bankService.update(command);
        return Result.success().message("更新题库成功！");
    }

    /**
     * 查看题库列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("list")
    public PageResult getBankList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return bankService.list(pageNum, pageSize);
    }


    /**
     * 删除题库
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delete")
    public Result deleteBank(@RequestParam("id") List<Long> ids) {
        bankService.delete(ids);
        return Result.success().message("删除题库成功！");
    }

    /**
     * 向题库中上传执行脚本
     */
    @PostMapping("script/{id}")
    public Result uploadScript(@PathVariable Long id, @RequestParam("script") MultipartFile file) {
        return bankService.uploadScript(id, file);
    }

    /**
     * 从题库中下载执行脚本
     */
    @GetMapping("script/{id}")
    public void downloadScript(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        //String fileName="upload.jpg";
        bankService.downloadScript(id,response);

    }

    /**
     * 从题库中下载执行脚本
     */
    @GetMapping("models/{id}")
    public void downloadModels(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        bankService.downloadModels(id,response);
    }

    /**
     * 向题库中上传模型
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("models/{id}")
    public Result uploadModels(@PathVariable Long id, HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("models");
        return bankService.uploadMutationModel(id, files);
    }

}
