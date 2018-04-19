package cn.edu.nju.software.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class ResetPasswordCommand {
    private Long userId;
    private String password;
}
