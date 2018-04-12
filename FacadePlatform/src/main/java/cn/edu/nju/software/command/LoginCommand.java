package cn.edu.nju.software.command;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/11 0011.
 */
@Getter
@Setter
public class LoginCommand {
    String password;
    String username;
    Boolean rememberMe;

    @Override
    public String toString() {
        return "LoginCommand{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
