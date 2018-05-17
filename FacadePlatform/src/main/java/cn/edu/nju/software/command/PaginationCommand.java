package cn.edu.nju.software.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/21 0021.
 */
@Getter
@Setter
public class PaginationCommand {
    //渲染次数,前端传入的
    private Integer draw = 1;
    private Integer pageSize = 10;
    //开始的条数
    private Integer start = 1;
    private Integer pageNum = 1;

    public PaginationCommand() {
    }

    public PaginationCommand(Integer pageSize, Integer start, Integer draw) {
        this.pageSize = pageSize;
        this.start = start;
        this.draw = draw;
        this.pageNum = start / pageSize + 1;
    }
}
