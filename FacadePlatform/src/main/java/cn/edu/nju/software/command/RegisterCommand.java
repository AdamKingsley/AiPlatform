package cn.edu.nju.software.command;

import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.util.Validate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

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

    public void validate() {
        if (StringUtils.isEmpty(username.trim())) {
            throw new ServiceException("用户名不能为空！");
        }
        if(! this.password.equals(confirmPassword)) {
            throw new ServiceException("两次输入的密码不同！");
        }
        if (!Validate.checkMail(mail)) {
            throw new ServiceException("邮箱格式不正确！");
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
