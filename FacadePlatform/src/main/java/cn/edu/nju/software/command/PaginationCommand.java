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
    private Integer pageSize=10;
    private Integer pageNum=1;

    public PaginationCommand() {
    }

    public PaginationCommand(Integer pageSize, Integer pageNum,Integer draw) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.draw = draw;
    }
}
