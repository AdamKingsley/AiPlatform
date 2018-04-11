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
 */

@Table(name = "t_user")
@Getter
@Setter
public class User extends IdEntity{

    private String username;
    private String password;
    private String salt;
    private String mail;
    private Integer roleId;
    private Integer state;
}
