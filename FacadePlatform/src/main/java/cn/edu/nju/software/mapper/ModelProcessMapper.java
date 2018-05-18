package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.ModelProcessDto;
import cn.edu.nju.software.entity.ModelProcess;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
public interface ModelProcessMapper extends Mapper<ModelProcess> {

    @Select("select distinct(model_id) from t_model_process where is_killed=1")
    List<Long> selectKilledModelIds(@Param("examId") Long examId, @Param("userId") Long userId);

    @Select("select max(iter) from t_model_process where model_id = #{modelId} and exam_id = #{examId} and user_id = #{userId}")
    Integer selectMaxIter(@Param("userId") Long userId, @Param("examId") Long examId, @Param("userId") Long modelId);

    @Select("select * from t_model_process where model_id = #{modelId} and exam_id = #{examId} and user_id = #{userId} and iter=#{iter}")
    ModelProcessDto selectByIter(Long userId, Long examId, Long modelId, Integer iter);
}
