package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.entity.Exercise;
import cn.edu.nju.software.mapper.ExamMapper;
import cn.edu.nju.software.mapper.ExerciseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
public class ExerciseService {
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExamMapper examMapper;

    //学生参加过的考试
    //未参加的考试
    //结束的考试

    public Result takeExam(Long userId, Long examId) {
        Exercise exercise = new Exercise();
        exercise.setUserId(userId);
        exercise.setExamId(examId);
        List<Exercise> exercises = exerciseMapper.select(exercise);
        return Result.success().message("");
    }
}
