package cn.edu.nju.software.command;

import lombok.Data;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Data
public class RegisterCommand {
    private String username;
    private String password;
    private String confirmPassword;
    private String mail;
    private Integer roleId;
    private Integer state;


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
