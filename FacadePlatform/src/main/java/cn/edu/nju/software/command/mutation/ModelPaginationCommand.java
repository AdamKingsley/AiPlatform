package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.command.PaginationCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/4/27 0027.
 */
@Getter
@Setter
public class ModelPaginationCommand extends PaginationCommand {

    private Date startTime;
    private Date endTime;
    private Integer type;
    private Long bankId;

    public ModelPaginationCommand() {
    }

    public ModelPaginationCommand(Integer pageNum, Integer pageSize,Integer draw) {
        super(pageNum, pageSize,draw);
    }
}
