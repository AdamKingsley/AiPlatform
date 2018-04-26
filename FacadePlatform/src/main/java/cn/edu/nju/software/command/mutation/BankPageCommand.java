package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.command.PageCommand;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/21 0021.
 */
@Getter
@Setter
public class BankPageCommand extends PageCommand {
    public BankPageCommand() {
    }

    public BankPageCommand(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
}
