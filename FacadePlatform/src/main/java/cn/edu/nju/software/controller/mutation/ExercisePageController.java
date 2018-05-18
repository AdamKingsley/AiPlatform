package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.service.mutation.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/mutation/exercise")
public class ExercisePageController {

    @Resource
    private ExerciseService exerciseService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String toExercise(@RequestParam(value = "userId", required = false) Long userId,
                             @RequestParam("examId") Long examId,
                             Model model) {
        ExerciseDto dto = exerciseService.takeExam(userId, examId);

        model.addAttribute("exercise", dto);
        return "mutation/student/exercise";
    }
}
