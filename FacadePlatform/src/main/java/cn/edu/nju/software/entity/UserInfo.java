package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/9 0009.
 */
@Getter
@Setter
public class UserInfo {
    private String username;
    private String password;
    private Integer state;
    private Integer roleType;
    private Long id;
    private String salt;
    private String roleName;
}
