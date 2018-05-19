package cn.edu.nju.software.mapper;

import cn.edu.nju.software.command.mutation.ModelPaginationCommand;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.entity.Model;
import com.github.pagehelper.Page;
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

    @Select({"<script>", "select m.*,b.script_location,b.standard_model_location from t_model m , t_bank b ",
            "where b.id = m.bank_id and m.id in ",
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
    List<ModelDto> selectByBankIds(@Param("ids") List<Long> ids);

    @Select("select count(1) from t_model where name=#{name} and bank_id=#{bankId}")
    Integer countByName(@Param("name") String name, @Param("bankId") Long bankId);


    @Select({"<script>",
            "select * from t_model where 1=1",
            "<if test='command.startTime!=null'>",
            "<![CDATA[ and create_time >= #{command.startTime}]]>",
            "</if>",
            "<if test='command.endTime!=null'>",
            "<![CDATA[ and create_time <= #{command.endTime}]]>",
            "</if>",
            "<if test='command.bankId!=null'>",
            "<![CDATA[ and bank_id = #{command.bankId}]]>",
            "</if>",
            "<if test='command.type!=null'>",
            "<![CDATA[ and type = #{command.type}]]>",
            "</if>",
            "</script>"})
    Page<ModelDto> selectModelPage(ModelPaginationCommand command);
}
