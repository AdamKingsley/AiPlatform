package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by mengf on 2018/4/10 0010.
 * 用户实体类
 */

@Table(name = "t_user")
@Getter
@Setter
public class User extends IdEntity {
    //用户名
    private String username;
    //密码
    private String password;
    //盐度
    private String salt;
    //邮箱
    private String mail;
    //用户类型
    private Integer roleId;
    //用户状态 未激活0 可用1 冻结2
    // (目前未有激活状态，所以该字段待定)
    private Integer state;
}
