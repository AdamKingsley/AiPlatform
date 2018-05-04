package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.mutation.ExamCommand;
import cn.edu.nju.software.command.mutation.ExamPaginationCommand;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageInfo;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.ExamDto;
import cn.edu.nju.software.entity.Exam;
import cn.edu.nju.software.mapper.ExamMapper;
import cn.edu.nju.software.mapper.SampleMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.ExampleMapper;

import java.util.Date;
import java.util.List;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private SampleMapper sampleMapper;

    public void create(ExamCommand command) {
        command.validate();
        Exam exam = new Exam();
        BeanUtils.copyProperties(command, exam);
        exam.setCreateTime(new Date());
        exam.setModifyTime(exam.getCreateTime());
        examMapper.insert(exam);
    }

    public void update(ExamCommand command) {
        command.validate();
        Exam exam = examMapper.selectByPrimaryKey(command.getId());
        //修改时间比考试结束时间还要大的话
        if (new Date().getTime() >= exam.getEndTime().getTime()) {
            throw new ServiceException("考试已经结束，禁止修改考试相关信息！");
        }
        BeanUtils.copyProperties(command, exam);
        exam.setModifyTime(new Date());
        examMapper.updateByPrimaryKey(exam);
    }

    public void delete(List<Long> ids) {
        for (Long id : ids) {
            examMapper.deleteByPrimaryKey(id);
            //TODO 删除考试相关的所有数据
        }
    }

    public PageResult list(ExamPaginationCommand command) {
        //查询分页数据
        PageHelper.startPage(command.getPageNum(), command.getPageSize());
        //Example example = new Example(ExamDto.class);
        //example.createCriteria();
        if (command.getType() != null && command.getIsMine() != null
                && command.getType() == 2 && command.getIsMine() == true) {
            //学生已经结束的
            Page<ExamDto> page = examMapper.selectStudentFinishedExamPage(command, ShiroUtils.currentUser().getId());
            PageInfo<ExamDto> pageInfo = new PageInfo<>(page);
            return new PageResult(pageInfo,command.getDraw());
        } else {
            Page<ExamDto> page = examMapper.selectExamPage(command);
            PageInfo<ExamDto> pageInfo = new PageInfo<>(page);
            return new PageResult(pageInfo,command.getDraw());
        }
    }


    public ExamDto getExam(Long id) {
        ExamDto dto = examMapper.selectById(id);
        return dto;
    }

}
