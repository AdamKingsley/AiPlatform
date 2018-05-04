package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.command.PaginationCommand;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/21 0021.
 */
@Getter
@Setter
public class BankPaginationCommand extends PaginationCommand {

    public BankPaginationCommand() {
    }

    public BankPaginationCommand(Integer pageNum, Integer pageSize, Integer draw) {
        super(pageNum, pageSize, draw);
    }

}
