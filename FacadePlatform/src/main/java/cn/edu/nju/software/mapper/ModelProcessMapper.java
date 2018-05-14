package cn.edu.nju.software.mapper;

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
}
