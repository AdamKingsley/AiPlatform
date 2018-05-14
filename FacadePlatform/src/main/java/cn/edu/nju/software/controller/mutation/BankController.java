package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.command.mutation.BankCommand;
import cn.edu.nju.software.command.mutation.BankPaginationCommand;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.BankDto;
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
     *
     * @return Result
     */
    @PostMapping("create")
    public Result addBank(HttpServletRequest request) {
        BankCommand command = new BankCommand();
        command.setName(request.getParameter("name"));
        command.setDescription(request.getParameter("description"));
        bankService.create(command, request);
        return Result.success().message("添加题库成功！");
    }

    @PostMapping("create/simple")
    public Result addBank(@RequestBody BankCommand command) {
        bankService.create(command);
        return Result.success().message("添加题库成功！");
    }

    /**
     * 更新题库基本信息
     *
     * @param id
     * @return
     */
    @PostMapping("update/{id}")
    public Result updateBank(@PathVariable Long id, HttpServletRequest request) {
        BankCommand command = new BankCommand();
        command.setId(id);
        command.setName(request.getParameter("name"));
        command.setDescription(request.getParameter("description"));
        bankService.update(command, request);
        return Result.success().message("更新题库成功！");
    }

    @PostMapping("update/{id}/simple")
    public Result updateBank(@PathVariable Long id, @RequestBody BankCommand command) {
        bankService.update(command);
        return Result.success().message("更新题库成功！");
    }

    /**
     * 查看题库列表
     * //TODO 加筛选条件 同理如AccountController
     *
     * @return
     */
    @PostMapping("list")
    public PageResult getBankList(@RequestBody BankPaginationCommand command) {
        //BankPaginationCommand command = new BankPaginationCommand(pageNum, pageSize, draw);
        return bankService.list(command);
    }


    @GetMapping("list/all")
    public Result getBankList() {
        List<BankDto> dtos = bankService.listAll();
        return Result.success().message("获取全部题库列表成功").withData(dtos);
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

    @GetMapping("detail/{id}")
    public Result getBank(@PathVariable("id") Long id) {
        BankDto dto = bankService.getBankDetail(id);
        return Result.success().message("获取题库详情成功").withData(dto);
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
        bankService.downloadScript(id, response);
    }

    /**
     * 从题库中下载执行脚本
     */
    @GetMapping("models/{id}")
    public void downloadModels(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        bankService.downloadModels(id, response);
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


    /**
     * 从题库中下载测试样本集
     */
    @GetMapping("samples/{id}")
    public void downloadSamples(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        bankService.downloadSamples(id, response);
    }

    /**
     * 向题库中上传模型
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("samples/{id}")
    public Result uploadSamples(@PathVariable Long id, HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("samples");
        return bankService.uploadSamples(id, files);
    }

}
