package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.entity.Exam;
import cn.edu.nju.software.entity.Exercise;
import cn.edu.nju.software.mapper.ExamMapper;
import cn.edu.nju.software.mapper.ExerciseMapper;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.google.common.collect.Interner;
import com.google.common.collect.Lists;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
public class ExerciseService {

    private static final String SEPERATE_CHARACTER = ",";
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExamMapper examMapper;

    //学生参加过的考试
    //未参加的考试
    //结束的考试
    public Result takeExam(Long userId, Long examId) {
        ExerciseDto dto = new ExerciseDto();
        Exercise exercise = new Exercise();
        exercise.setUserId(userId);
        exercise.setExamId(examId);
        //获取考试信息
        Exam exam = examMapper.selectByPrimaryKey(examId);
        //如果已经结束考试
        if (exam.getEndTime().getTime() <= new Date().getTime()) {
            throw new ServiceException("当前考试已经结束，无法进入！");
        }
        List<Exercise> exercises = exerciseMapper.select(exercise);
        if (exercises != null && exercises.size() > 0) {
            //之前参加了的考试还没结束
            exercise = exercises.get(0);
            BeanUtils.copyProperties(exercise, dto);


        } else {
            //第一次参加该考试要新建exercise对象
        }
        return Result.success().message("获取相关考试信息成功！").withData(dto);
    }


    private List<Long> getIds(String ids) {
        String[] idArray = ids.split(SEPERATE_CHARACTER);
        List<Long> idList = Lists.newArrayList();
        for (String id : idArray) {
            idList.add(Long.parseLong(id));
        }
        return idList;
    }
}
