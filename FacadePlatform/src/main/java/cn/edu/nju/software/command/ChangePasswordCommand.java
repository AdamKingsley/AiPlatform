package cn.edu.nju.software.command;

import cn.edu.nju.software.common.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Getter
@Setter
public class ChangePasswordCommand {

    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;

    public void validate() {
        if(! this.password.equals(confirmPassword)) {
            throw new ServiceException("密码不一致！");
        }
    }
}
