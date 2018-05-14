package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.command.PaginationCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/5/8 0008.
 */
@Getter
@Setter
public class ProcessPaginationCommand extends PaginationCommand {
    private Date startTime;
    private Date endTime;
}
