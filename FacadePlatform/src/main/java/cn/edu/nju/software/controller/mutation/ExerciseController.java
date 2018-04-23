package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.service.mutation.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@RestController
@RequestMapping("exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("enter")
    public Result takeExam(@RequestParam("userId") Long userId, @RequestParam("examId") Long examId) {
        return exerciseService.takeExam(userId, examId);
    }
}
