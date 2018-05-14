package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/5/14 0014.
 */
@Getter
@Setter
public class ModelKillRateDto {
    //模型id
    private Long id;
    //模型名称
    private String name;
    //模型process的次数
    private Long nums;
    //模型杀死的次数
    private Long killedNums;
    //模型杀死率
    private Double killRate;
}
