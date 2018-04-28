package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.command.PaginationCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/4/21 0021.
 */
@Getter
@Setter
public class ExamPaginationCommand extends PaginationCommand {
    //0还未开始；1正在进行；2已经结束 3全部的
    private Integer type;
    //指全部的还是只是我参加过
    private Boolean isMine;
    //考试开始时间介于某个区间内
    private Date startTime;
    private Date endTime;
}
