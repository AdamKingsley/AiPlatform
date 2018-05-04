package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/5/2 0002.
 */
@Getter
@Setter
public class SampleDto extends IdDto {
    private String name;
    private Long bankId;
    private String location;
    //暂时无用
    private Integer type;
}
