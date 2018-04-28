package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.service.mutation.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
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
        return exerciseService.takeExam(userId, examId);
    }

    @PostMapping("upload/sample")
    public Result uploadSample(@RequestParam("userId") Long userId, @RequestParam("examId") Long examId, HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("sample");
        return exerciseService.uploadSample(userId, examId, files);
    }

}
