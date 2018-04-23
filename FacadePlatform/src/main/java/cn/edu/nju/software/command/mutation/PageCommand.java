package cn.edu.nju.software.command.mutation;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/21 0021.
 */
@Getter
@Setter
public class PageCommand {
    private Integer pageSize=10;
    private Integer pageNum=1;

    public PageCommand() {
    }

    public PageCommand(Integer pageSize, Integer pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}
