package cn.edu.nju.software.mapper;

import cn.edu.nju.software.entity.ModelProcess;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
@Mapper
public interface ModelProcessMapper extends BaseMapper<ModelProcess>{
}
