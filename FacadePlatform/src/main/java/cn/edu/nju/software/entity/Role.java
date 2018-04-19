package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

/**
 * Created by mengf on 2018/4/19 0019.
 * 用户类型表
 */
@Table(name = "t_role")
@Getter
@Setter
public class Role extends IdEntity {
    //目前只有普通用户和管理员
    //以后可扩充
    private String name;
}
