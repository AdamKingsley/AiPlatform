package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by mengf on 2018/5/8 0008.
 */
@Getter
@Setter
public class ExamResultDto {
    //参加考试的用户
    private Integer counts;
    //模型杀死率列表
    private List<ModelKillRateDto> killRates;
    //用户排名列表
    private List<RankDto> ranks;

}
