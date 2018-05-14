package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.entity.Bank;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
public interface BankMapper extends Mapper<Bank> {
    @Select("select count(1) from t_bank where deleted=0 and name=#{name} ")
    Integer countByName(@Param("name") String name);

    @Select("select * from t_bank where deleted = 0 order by modify_time desc")
    Page<BankDto> selectPage();

    @Update("update t_bank set nums = nums - #{num}")
    void subModelNums(@Param("num") int num);

    @Select("select * from t_bank where deleted = 0 and id=#{id}")
    BankDto selectById(@Param("id") Long id);

    @Select("select * from t_bank where deleted = 0 ")
    List<BankDto> selectAllBanks();


    @Select({"<script>", "select b.* from t_bank b ",
            "where deleted=0 and b.id in ",
            "<foreach item='item' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<BankDto> selectInBankIds(@Param("ids") List<Long> ids);


    @Select({"<script>", "select b.* from t_bank b ",
            "where deleted=0 and b.id not in ",
            "<foreach item='item' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<BankDto> selectNotInBankIds(@Param("ids") List<Long> ids);
}
