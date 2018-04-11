package cn.edu.nju.software.entity;

import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

/**
 * Created by mengf on 2018/4/10 0010.
 */
@Getter
@Setter
public class IdEntity {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
}
