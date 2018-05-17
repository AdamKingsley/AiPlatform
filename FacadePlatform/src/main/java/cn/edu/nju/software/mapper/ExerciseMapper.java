package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.entity.Exercise;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository

public interface ExerciseMapper extends Mapper<Exercise> {

    @Select("select * from t_exercise where user_id=#{userId} and exam_id=#{examId}")
    Exercise selectByUserAndExam(@Param("userId") Long userId, @Param("examId") Long examId);

    @Select("select * from t_exercise e ,t_user u where e.user_id=u.id and exam_id=#{examId}")
    List<ExerciseDto> selectExerciseByExam(@Param("examId") Long examId);
}
