package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mengf on 2018/4/19 0019.
 * 用户考试详情表
 */
@Table(name = "t_exercise")
@Getter
@Setter
public class Exercise extends IdEntity {
    //考试分配给该用户的模型id集合
    private String modelIds;
    //用户正式进入考试的时间
    private Date startTime;
    //用户结束考试的时间
    private Date endTime;
    //用户杀死的变异体的数量
    private Integer killNums;
    //用户总共进行了多少次迭代
    private Integer totalIters;
    //用户的考试ID-参加了哪场考试
    private Long examId;
    //用户ID
    private Long userId;
    //用户杀死的模型的ID 总数量同killNums
    private String killModelIds;
    //考试状态 0还未开始 1正在进行 2已结束
    private Integer state;
}
