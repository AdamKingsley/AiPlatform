package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class ModelDto extends IdDto {
    private String location;
    private Integer type;
    private String name;
    private Boolean isKilled;
}
