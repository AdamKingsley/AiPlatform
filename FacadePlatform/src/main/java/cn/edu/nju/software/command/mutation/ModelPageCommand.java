package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.command.PageCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/4/27 0027.
 */
@Getter
@Setter
public class ModelPageCommand extends PageCommand {

    private Date startTime;
    private Date endTime;
    private Integer type;
    private Long bankId;

    public ModelPageCommand() {
    }

    public ModelPageCommand(Integer pageNum, Integer pageSize) {
        super(pageNum, pageSize);
    }
}
