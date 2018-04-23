package cn.edu.nju.software.mapper;

import cn.edu.nju.software.dto.ExamDto;
import cn.edu.nju.software.entity.Exam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
@Mapper
public interface ExamMapper extends BaseMapper<Exam> {

    @Select("select * from t_exam order by create_time desc")
    Page<ExamDto> selectPage();
}
