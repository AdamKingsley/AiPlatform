package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.entity.Bank;
import cn.edu.nju.software.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.Map;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
@Mapper
public interface BankMapper extends BaseMapper<Bank> {
    @Select("select count(1) from t_bank where name=#{name} ")
    Integer countByName(@Param("name") String name);

    @Select("select * from t_bank order by modify_time desc")
    Page<BankDto> selectPage();

}
