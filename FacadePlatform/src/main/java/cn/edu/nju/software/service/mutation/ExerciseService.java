package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.entity.Exam;
import cn.edu.nju.software.entity.Exercise;
import cn.edu.nju.software.mapper.ExamMapper;
import cn.edu.nju.software.mapper.ExerciseMapper;
import cn.edu.nju.software.mapper.ModelMapper;
import cn.edu.nju.software.util.RandomUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
public class ExerciseService {

    private static final String SEPERATOR = ",";
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExamMapper examMapper;
    @Autowired
    private ModelMapper modelMapper;

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
            List<Long> modelIds = getIds(dto.getModelIds());
            List<ModelDto> modelDtos = modelMapper.selectByModelIds(modelIds);
            //设置获取到的modelDto的isKilled属性
            if (StringUtils.isEmpty(exercise.getKillModelIds())) {
                modelDtos.forEach(model -> model.setIsKilled(false));
            } else {
                List<Long> killedModelIds = getIds(exercise.getModelIds());
                for (ModelDto modelDto : modelDtos) {
                    if (killedModelIds.contains(modelDto.getId().longValue())) {
                        modelDto.setIsKilled(true);
                    }
                }
            }
            dto.setModelDtos(modelDtos);
        } else {
            //第一次参加该考试要新建exercise对象
            //首先要从题库中选择模型出来
            List<Long> bankIds = getIds(exam.getBankIds());
            List<ModelDto> total_modelDtos = modelMapper.selectByBankIds(bankIds);
            List<ModelDto> exercise_models = getRandomModels(total_modelDtos, exam.getModelNums());
            //未被杀死 init
            exercise_models.forEach(model -> model.setIsKilled(false));
            //将模型添加到exerciseDto
            dto.setModelDtos(exercise_models);
            //设置基础信息
            dto.setStartTime(new Date());
            dto.setKillNums(0);
            dto.setTotalIters(0);
            //设置题库ID们
            dto.setModelIds(getIdsStr(exercise_models));
            BeanUtils.copyProperties(dto, exercise);
            exerciseMapper.insert(exercise);
        }
        return Result.success().message("获取学生本场考试信息成功！").withData(dto);
    }

    private List<ModelDto> getRandomModels(List<ModelDto> modelDtos, int num) {
        List<ModelDto> result = Lists.newArrayList();
        //模型数量不足的话 就将模型全部都用上
        if (modelDtos.size() <= num) {
            result.addAll(modelDtos);
            return result;
        }
        //模型数量充足的话 随机选择不重复的num个
        int exclude_num = modelDtos.size() - num;
        if (exclude_num >= num) {
            //如果选少量的话直接选
            int[] array = RandomUtil.randomArray(0, modelDtos.size() - 1, num);
            for (int i : array) {
                result.add(modelDtos.get(i));
            }
            return result;
        } else {
            //如果选比较大量的话直接剔除
            int[] array = RandomUtil.randomArray(0, modelDtos.size() - 1, exclude_num);
            List<Integer> excludeList = Lists.newArrayList();
            for (int i : array) {
                excludeList.add(i);
            }
            for (int i = 0; i < modelDtos.size(); i++) {
                if (!excludeList.contains(i)) {
                    result.add(modelDtos.get(i));
                }
            }
            return result;
        }
    }

    private String getIdsStr(Iterable<String> ids) {
        String result = "";
        for (String id : ids) {
            result += id;
            result += SEPERATOR;
        }
        if (result.lastIndexOf(SEPERATOR) == result.length() - 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }


    private String getIdsStr(List<ModelDto> models) {
        String result = "";
        for (ModelDto dto : models) {
            result += dto.getId();
            result += SEPERATOR;
        }
        if (result.lastIndexOf(SEPERATOR) == result.length() - 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private List<Long> getIds(String ids) {
        String[] idArray = ids.split(SEPERATOR);
        List<Long> idList = Lists.newArrayList();
        for (String id : idArray) {
            idList.add(Long.parseLong(id));
        }
        return idList;
    }
}
