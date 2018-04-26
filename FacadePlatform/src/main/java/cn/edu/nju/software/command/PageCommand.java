package cn.edu.nju.software.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/21 0021.
 */
@Getter
@Setter
public class PageCommand {
    private Integer pageSize = 10;
    private Integer pageNum = 1;

    public PageCommand() {
    }

    public PageCommand(Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageNum == null ||
                pageNum == 0 || pageSize == 0) {
            this.pageSize = 10;
            this.pageNum = 1;
        } else {
            this.pageSize = pageSize;
            this.pageNum = pageNum;
        }
    }
}
