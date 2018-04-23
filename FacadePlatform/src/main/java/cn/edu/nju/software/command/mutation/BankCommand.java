package cn.edu.nju.software.command.mutation;

import cn.edu.nju.software.common.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class BankCommand {
    private Long id;
    private String name;
    private String description;

    public void validate() {
        if (StringUtils.isEmpty(name.trim())) {
            throw new ServiceException("题库名称不能为空！");
        }
    }

}
