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

    public static RoleEnum valueOf(int value) {
        switch (value) {
            case 1:
                return RoleEnum.ADMIN;
            case 2:
                return RoleEnum.TEACHER;
            case 3:
                return RoleEnum.STUDENT;
            default:
                return null;
        }
    }

    public int getRoleId() {
        return roleId;
    }
}
