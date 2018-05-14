package cn.edu.nju.software.command.mutation;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/27 0027.
 */
@Getter
@Setter
public class ModelCommand {

    private Long id;
    //模型名称 看来update model估计只能修改模型名称了
    private String name;
}
