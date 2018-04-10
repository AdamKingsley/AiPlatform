package cn.edu.nju.software.service;

import cn.edu.nju.software.entity.TestEntity;
import cn.edu.nju.software.entity.UserFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengf on 2018/4/2 0002.
 */

@Service
public class TestService {


    public TestEntity get() {
        TestEntity entity = new TestEntity("hello", "world");
        return entity;
    }

    public List<TestEntity> getList() {
        TestEntity entity1 = new TestEntity("liming", "123456");
        TestEntity entity2 = new TestEntity("wangfei", "456789");
        ArrayList<TestEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        return entities;
    }

}
