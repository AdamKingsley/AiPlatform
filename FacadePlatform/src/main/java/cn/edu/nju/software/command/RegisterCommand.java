package cn.edu.nju.software.command;

import cn.edu.nju.software.common.exception.ServiceException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Data
@Getter
@Setter
public class RegisterCommand {
    private String username;
    private String password;
    private String confirmPassword;
    private String mail;
    private Integer roleId;
    private Integer state;

    public void validate() {
        if(! this.password.equals(confirmPassword)) {
            throw new ServiceException("两次输入的密码不同！");
        }
    }


    @Override
    public String toString() {
        return "RegisterCommand{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
