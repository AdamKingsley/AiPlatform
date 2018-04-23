package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.common.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.lang.management.LockInfo;
import java.util.Date;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class ExamCommand {
    private Long id;
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
    //允许提前多少分钟进入考试
    // (start_time-pre_minutes ~ start_time
    // 都是可以进入考试的)
    //只不过如果还没有开始的话提示还没到考试时间
    private Integer preMinute;
    //变异体的数量
    private Integer modelNums;
    //题库集合
    private String bankIds;


    public void validate() {
        if (StringUtils.isEmpty(name.trim())) {
            throw new ServiceException("考试名称不能为空！");
        }
        if (this.endTime.getTime() <= this.startTime.getTime()) {
            throw new ServiceException("开始时间要小于结束时间！");
        }
        if (this.maxItems <= 0) {
            throw new ServiceException("最大上传的样本量应大于0！");
        }
        if (this.maxIters <= 0) {
            throw new ServiceException("测试的轮数需要大于0！");
        }
        if (StringUtils.isEmpty(bankIds.trim())) {
            throw new ServiceException("必须选择测试考试的题库！");
        }
    }

}
