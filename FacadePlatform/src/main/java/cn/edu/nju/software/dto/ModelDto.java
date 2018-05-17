package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class ModelDto extends IdDto {
    private Long bankId;
    private String location;
    //对比的标准的模型
    private String standardModelLocation;
    private Integer type;
    private String name;
    private Boolean isKilled;
    private Date createTime;
    private Date modifyTime;
    //对应执行脚本所在的location
    private String scriptLocation;
}
