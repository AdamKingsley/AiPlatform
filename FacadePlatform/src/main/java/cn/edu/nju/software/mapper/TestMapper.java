package cn.edu.nju.software.mapper;

import cn.edu.nju.software.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by mengf on 2018/4/5 0005.
 */
@Mapper
public interface TestMapper {
    TestEntity queryById(Integer Id);

    TestEntity queryByName(String name);
}
