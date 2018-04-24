package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Getter
@Setter
@NameStyle(value = Style.camelhumpAndLowercase)
public class UserDto extends IdDto {

    private String username;
    private String mail;
    private Integer roleId;
    private Integer state;
    private Date createTime;
    private Date modifyTime;

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
