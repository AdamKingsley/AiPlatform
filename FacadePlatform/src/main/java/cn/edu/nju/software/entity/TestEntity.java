package cn.edu.nju.software.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mengf on 2018/4/2 0002.
 */
@Data
public class TestEntity {
    private Integer id;
    private String name;
    private String password;

    public TestEntity(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public TestEntity() {
    }
}
