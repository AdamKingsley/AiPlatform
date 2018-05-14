package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.SampleDto;
import cn.edu.nju.software.entity.Sample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by mengf on 2018/4/24 0024.
 */
public interface SampleMapper extends Mapper<Sample> {

    @Select("select count(1) from t_sample where bank_id=#{bankId}")
    Long countByBankId(@Param("bankId") Long bankId);

    @Select({"<script>", "select * from t_sample where bank_id in ",
            "<foreach item='item' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<SampleDto> selectByBankIds(@Param("ids") List<Long> ids);
}
