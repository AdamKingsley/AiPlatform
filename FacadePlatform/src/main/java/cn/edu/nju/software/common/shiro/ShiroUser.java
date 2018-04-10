package cn.edu.nju.software.common.shiro;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by mengf on 2018/4/9 0009.
 */
@Getter
@Setter
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;

    private Long id;
    private String username;
    //private String password;
    private String mail;
    //private String tel;
    //角色名称 考生/管理员etc
    private String roleName;
    //角色对应的编号
    private Integer roleType;
    //角色账号状态 冻结否之类的
    private Integer state;

    public ShiroUser() {
    }

    public ShiroUser(Long id, String username, String mail, String roleName, Integer roleType, Integer state) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.roleName = roleName;
        this.roleType = roleType;
        this.state = state;
    }

    @Override
    public String toString() {
        return "ShiroUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleType=" + roleType +
                ", state=" + state +
                '}';
    }
}
