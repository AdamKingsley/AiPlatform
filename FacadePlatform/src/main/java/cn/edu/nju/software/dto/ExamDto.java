package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class ExamDto extends IdDto {
    //考试的名称
    private String name;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //考试限制，允许最多的迭代次数
    // 即最多允许多少次提交答案
    // 0不限制
    private Integer maxIters;
    //考试限制，允许每次提交样本的数量限制
    // 0不限制
    private Integer maxItems;
    //变异体的数量
    private Integer modelNums;
    //description
    private String description;
}
