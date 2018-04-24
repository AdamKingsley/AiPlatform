package cn.edu.nju.software.common.shiro;


/**
 * Created by mengf on 2018/4/24 0024.
 */
public enum RoleEnum {
    ADMIN(1), TEACHER(2), STUDENT(3);

    private Integer roleId;


    RoleEnum(Integer roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
