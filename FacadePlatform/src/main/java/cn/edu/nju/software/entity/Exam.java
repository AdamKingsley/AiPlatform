package cn.edu.nju.software.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mengf on 2018/4/18 0018.
 * 考试测试实体类
 */
@Table(name = "t_exam")
@Getter
@Setter
public class Exam extends IdEntity {
    //考试的名称
    private String name;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //创建时间
    private Date createTime;
    //考试限制，允许最多的迭代次数
    // 即最多允许多少次提交答案
    // 0不限制
    private Integer max_iters;
    //考试限制，允许每次提交样本的数量限制
    // 0不限制
    private Integer max_items;
    //允许提前多少分钟进入考试
    // (start_time-pre_minutes ~ start_time
    // 都是可以进入考试的)
    //只不过如果还没有开始的话提示还没到考试时间
    private Integer preMinute;
    //变异体的数量
    private Integer mutation_nums;
    //题库集合
    private String bankIds;
}
