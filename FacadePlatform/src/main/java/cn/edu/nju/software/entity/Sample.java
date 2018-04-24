package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

/**
 * Created by mengf on 2018/4/24 0024.
 */
@Table(name = "t_sample")
@Getter
@Setter
public class Sample extends IdEntity {
    private String name;
    private Long bankId;
    private String location;
    //暂时无用
    private Integer type;
}
