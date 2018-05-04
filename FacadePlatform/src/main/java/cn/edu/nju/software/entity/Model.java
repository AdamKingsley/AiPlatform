package cn.edu.nju.software.entity;

import cn.edu.nju.software.common.shiro.Login;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mengf on 2018/4/19 0019.
 * 变异模型表
 */
@Table(name = "t_model")
@Getter
@Setter
public class Model extends IdEntity {
    //name
    private String name;
    //所属的题库ID
    private Long bankId;
    //所在的位置
    private String location;
    //变异的类型（0改参数 1改神经元数量 2改变激励函数）
    private Integer type;
    private Date createTime;
    private Date modifyTime;
}
