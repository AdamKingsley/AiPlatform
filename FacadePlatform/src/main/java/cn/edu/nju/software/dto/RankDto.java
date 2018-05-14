package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/5/14 0014.
 */
@Getter
@Setter
public class RankDto {
    //用户id
    private Long userId;
    //用户名
    private String username;
    //考试的变异模型
    private String modelIds;
    //考试的变异模型的数量
    private Integer modelNums;
    //杀死的变异模型
    private String killModelIds;
    //杀死的变异模型的数量
    private Integer killModelNums;
    //模型杀死率
    private Double killRate;
}
