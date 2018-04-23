package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.entity.Bank;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
public interface BankMapper extends Mapper<Bank> {
    @Select("select count(1) from t_bank where name=#{name} ")
    Integer countByName(@Param("name") String name);

    @Select("select * from t_bank order by modify_time desc")
    Page<BankDto> selectPage();

    Page<BankDto> selectByExample(@Param("example")Example example);

}
