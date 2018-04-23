package cn.edu.nju.software.mapper;

import cn.edu.nju.software.common.result.PageInfo;
import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select count(1) from t_user where username=#{username} ")
    Integer countByUsername(@Param("username") String username);

    @Select("select count(1) from t_user where mail=#{mail} ")
    Integer countByMail(@Param("mail") String mail);

    @Select("select * from t_user")
    Page<UserDto> selectPage();

    @Select("select * from t_user where mail=#{mail}")
    UserDto selectByMail(@Param("mail") String mail);

    @Select("select * from t_user where username=#{username}")
    User selectByUsername(@Param("username") String username);
}
