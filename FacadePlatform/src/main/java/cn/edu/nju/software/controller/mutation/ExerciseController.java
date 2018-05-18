package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.service.mutation.ExerciseService;
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
@RequestMapping("exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("enter")
    public Result takeExam(@RequestParam(value = "userId", required = false) Long userId, @RequestParam("examId") Long examId) {
        if (userId == null) {
            if (ShiroUtils.currentUser() == null) {
                return Result.error().exception(ExceptionEnum.LOGIN_INVALID);
            } else {
                userId = ShiroUtils.currentUser().getId();
            }
        }
        ExerciseDto dto = exerciseService.takeExam(userId, examId);
        return Result.success().message("获取学生本场考试信息成功！").withData(dto);
    }

    @PostMapping("upload/sample")
    public Result uploadSample(@RequestParam("userId") Long userId, @RequestParam("examId") Long examId, HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("sample");
        ExerciseDto dto = exerciseService.uploadSample(userId, examId, files);
        return Result.success().message("上传样本测试成功").withData(dto);
    }


    /**
     * 下载考试参考样本samples即下载本场考试相关的
     *
     * @param examId
     */
    @GetMapping("download/reference-samples")
    public void downloadReferenceSamples(@RequestParam("examId") Long examId, HttpServletResponse response) {
        exerciseService.downloadReferenceSamples(examId, response);
    }

}
