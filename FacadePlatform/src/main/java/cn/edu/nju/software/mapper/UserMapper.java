package cn.edu.nju.software.mapper;

import cn.edu.nju.software.command.UserPageCommand;
import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.entity.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Repository

public interface UserMapper extends Mapper<User> {

    @Select("select count(1) from t_user where username=#{username} ")
    Integer countByUsername(@Param("username") String username);

    @Select("select count(1) from t_user where mail=#{mail} ")
    Integer countByMail(@Param("mail") String mail);

    @Select("select * from t_user")
    Page<UserDto> selectPage();

    @Select("select * from t_user where mail=#{mail}")
    UserDto selectByMail(@Param("mail") String mail);

    @Select("select * from t_user u,t_role r where username=#{username} where u.role_id=r.id")
    UserDto selectByUsername(@Param("username") String username);

    @Select({"<script>",
            "select * from t_user where 1=1",
            "<if test='command.startCreateTime!=null'>",
            "<![CDATA[ and create_time >= #{command.startCreateTime}]]>",
            "</if>",
            "<if test='command.endCreateTime!=null'>",
            "<![CDATA[ and create_time <= #{command.endCreateTime}]]>",
            "</if>",
            "<if test='command.startModifyTime!=null'>",
            "<![CDATA[ and modify_time >= #{command.startModifyTime}]]>",
            "</if>",
            "<if test='command.endModifyTime!=null'>",
            "<![CDATA[ and modify_time <= #{command.endModifyTime} ]]>",
            "</if>",
            "<if test='command.state!=null and command.state>=0'>",
            "<![CDATA[ and state= #{command.state} ]]>",
            "</if>",
            "</script>"})
    Page<UserDto> selectUserPage(@Param("command") UserPageCommand command);
}
