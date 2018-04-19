package cn.edu.nju.software.mapper;

import cn.edu.nju.software.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {
}
