package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Getter
@Setter
public class UserDto extends IdDto{
    private String username;
    private String mail;
    private String roleId;
    private String state;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + super.getId() +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", roleId='" + roleId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
