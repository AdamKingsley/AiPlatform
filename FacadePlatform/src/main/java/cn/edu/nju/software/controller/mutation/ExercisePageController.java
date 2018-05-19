package cn.edu.nju.software.controller.mutation;

import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.ExamDto;
import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.service.mutation.ExamService;
import cn.edu.nju.software.service.mutation.ExerciseService;
import cn.edu.nju.software.service.mutation.ModelProcessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/mutation/exercise")
public class ExercisePageController {

    @Resource
    private ExerciseService exerciseService;
    @Resource
    private ExamService examService;
    @Resource
    private ModelProcessService modelProcessService;

    @RequestMapping(method = RequestMethod.GET)
    public String toExercise(@RequestParam(value = "userId", required = false) Long userId,
                             @RequestParam("examId") Long examId,
                             Model model) {
//        userId = 5L;
        ShiroUser user = ShiroUtils.currentUser();
        ExerciseDto dto = exerciseService.takeExam(userId, examId);
        ExamDto exam = examService.getExam(dto.getExamId());

        if (dto.getKillModelIds() != null) {
            List<String> killedM = Arrays.asList(dto.getKillModelIds().split(","));
            for (ModelDto m: dto.getModelDtos()) {
                m.setIsKilled(false);
                for (String s: killedM) {
                    if (!s.equals("") && m.getId() == Long.parseLong(s)) {
                        m.setIsKilled(true);
                        break;
                    }
                }
            }
        }

        model.addAttribute("exercise", dto);
        model.addAttribute("exam", exam);
        model.addAttribute("user", user);
        return "mutation/student/exercise";
    }

    @RequestMapping(value = "/result/{userId}/{examId}/{modelId}", method = RequestMethod.GET)
    public String toExerciseResult(@PathVariable("userId") Long userId,
                                   @PathVariable("examId") Long examId,
                                   @PathVariable("modelId") Long modelId,
                                   Model model) {
        ShiroUser user = ShiroUtils.currentUser();

        Integer iters = modelProcessService.getIters(userId, examId, modelId);

        model.addAttribute("userId", userId);
        model.addAttribute("examId", examId);
        model.addAttribute("modelId", modelId);
        model.addAttribute("iters", iters);
        model.addAttribute("user", user);
        return "mutation/student/result";
    }
}
