package cn.edu.nju.software.mapper;

import cn.edu.nju.software.entity.Model;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Mapper
@Repository
public interface ModelMapper extends BaseMapper<Model> {

    @Delete("delete from t_model where bank_id=#{bank_id}")
    void deleteByBankId(@Param("bank_id") Long bankId);
}
