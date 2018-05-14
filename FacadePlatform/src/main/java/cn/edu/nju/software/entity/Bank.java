package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mengf on 2018/4/19 0019.
 * 题库实体类
 */
@Getter
@Setter
@Table(name = "t_bank")
public class Bank extends IdEntity {
    //题库的名称
    private String name;
    //题库的变异体数量
    private Integer nums;
    //题库对应变异体运行脚本的位置
    private String scriptLocation;
    //题库创建时间
    private Date createTime;
    //题库修改时间
    private Date modifyTime;
    //备注
    private String description;
    private Boolean deleted;
}
