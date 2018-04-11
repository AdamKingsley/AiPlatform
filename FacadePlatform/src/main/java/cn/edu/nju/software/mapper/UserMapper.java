package cn.edu.nju.software.mapper;

import cn.edu.nju.software.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select count(1) from t_user where username=#{username} ")
    Integer countByUsername(@Param("username") String username);

    @Select("select count(1) from t_user where mail=#{mail} ")
    Integer countByMail(@Param("mail") String mail);
}
