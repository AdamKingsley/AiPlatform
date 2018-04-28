package cn.edu.nju.software.command;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/4/24 0024.
 */
@Getter
@Setter
public class UserPaginationCommand extends PaginationCommand {
    private Date startCreateTime;
    private Date endCreateTime;
    private Date startModifyTime;
    private Date endModifyTime;
    private Integer state; //0,1,2 //-1 all

    public UserPaginationCommand() {
    }

    public UserPaginationCommand(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
}
