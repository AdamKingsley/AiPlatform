package cn.edu.nju.software.mapper;

import cn.edu.nju.software.command.mutation.ExamPaginationCommand;
import cn.edu.nju.software.dto.ExamDto;
import cn.edu.nju.software.entity.Exam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Repository
public interface ExamMapper extends Mapper<Exam> {
    @Select("select * from t_exam order by create_time desc")
    Page<ExamDto> selectPage();

    @Select("select * from t_exam where id = #{id}")
    ExamDto selectById(@Param("id") Long id);


    @Select({"<script>",
            "select e.* from t_exam e where 1=1",
            "<choose>",
            "<when test='command.type==0 || command.type==null'>",
            "<![CDATA[ and e.start_time > #{command.currentTime} ]]>",
            "</when>",
            "<when test='command.type==1'>",
            "<![CDATA[ and e.start_time<= #{command.currentTime} and e.end_time >= #{command.currentTime}]]>",
            "</when>",
            "<when test='command.type==2'>",
            "<![CDATA[ and e.end_time < #{command.currentTime} ]]>",
            "</when>",
            "</choose>",
            "<if test='command.startTime!=null'>",
            "<![CDATA[ and e.start_time >= #{command.startTime}]]>",
            "</if>",
            "<if test='command.endTime!=null'>",
            "<![CDATA[ and e.start_time <= #{command.endTime}]]>",
            "</if>",
            "</script>"})
    Page<ExamDto> selectExamPage(@Param("command") ExamPaginationCommand command);

    @Select({
            "<script>",
            "select e.* from t_exam e,t_exercise exer where e.id=exer.exam_id and exer.user_id=#{userId}",
            "<![CDATA[ and e.end_time < #{command.currentTime} ]]>",
            "<if test='command.startTime!=null'>",
            "<![CDATA[ and e.start_time >= #{command.startTime}]]>",
            "</if>",
            "<if test='command.endTime!=null'>",
            "<![CDATA[ and e.start_time <= #{command.endTime}]]>",
            "</if>",
            "</script>"
    })
    Page<ExamDto> selectStudentFinishedExamPage(@Param("command") ExamPaginationCommand command,@Param("userId")Long userId);
}
