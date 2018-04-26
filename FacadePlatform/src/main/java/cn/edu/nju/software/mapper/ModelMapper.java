package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.entity.Model;
import org.apache.ibatis.annotations.Delete;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by mengf on 2018/4/19 0019.
 */

@Repository
public interface ModelMapper extends Mapper<Model> {

    @Delete("delete from t_model where bank_id=#{bank_id}")
    void deleteByBankId(@Param("bank_id") Long bankId);

    @Select({"<script>", "select * from t_model where id in ",
            "<foreach item='item' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<ModelDto> selectByModelIds(@Param("ids") List<Long> ids);

    @Select({"<script>", "select * from t_model where bank_id in ",
            "<foreach item='item' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<ModelDto> selectByBankIds(List<Long> ids);
}
