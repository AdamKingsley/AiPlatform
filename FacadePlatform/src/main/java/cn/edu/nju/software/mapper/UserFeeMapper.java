package cn.edu.nju.software.mapper;

import cn.edu.nju.software.entity.UserFee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFee record);

    int insertSelective(UserFee record);

    UserFee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFee record);

    int updateByPrimaryKey(UserFee record);
}