package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.PaginationCommand;
import cn.edu.nju.software.command.mutation.ExamCommand;
import cn.edu.nju.software.command.mutation.ExamPaginationCommand;
import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageInfo;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.RoleEnum;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.common.shiro.StateEnum;
import cn.edu.nju.software.dto.*;
import cn.edu.nju.software.entity.Bank;
import cn.edu.nju.software.entity.Exam;
import cn.edu.nju.software.entity.Exercise;
import cn.edu.nju.software.mapper.*;
import cn.edu.nju.software.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.ExampleMapper;

import java.util.*;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    public void create(ExamCommand command) {
        command.validate();
        Exam exam = new Exam();
        BeanUtils.copyProperties(command, exam);
        exam.setCreateTime(new Date());
        exam.setModifyTime(exam.getCreateTime());
        ShiroUser user = ShiroUtils.currentUser();
        //如果没登陆
        if (user == null) {
            throw new ServiceException(ExceptionEnum.LOGIN_INVALID);
        }
        //如果是老师
        if (user.getRoleId().longValue() == RoleEnum.TEACHER.getRoleId()) {
            exam.setCreateUserId(user.getId());
            exam.setModifyUserId(user.getId());
            examMapper.insert(exam);
        } else {
            throw new ServiceException(ExceptionEnum.PERMISSION_DENIED);
        }
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
        ShiroUser user = ShiroUtils.currentUser();
        //如果没登陆
        if (user == null) {
            throw new ServiceException(ExceptionEnum.LOGIN_INVALID);
        }
        //如果是老师
        if (user.getRoleId().longValue() == RoleEnum.TEACHER.getRoleId()) {
            exam.setModifyUserId(user.getId());
            examMapper.updateByPrimaryKey(exam);
        } else {
            throw new ServiceException(ExceptionEnum.PERMISSION_DENIED);
        }
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
            return new PageResult(pageInfo, command.getDraw());
        } else {
            Page<ExamDto> page = examMapper.selectExamPage(command);
            PageInfo<ExamDto> pageInfo = new PageInfo<>(page);
            return new PageResult(pageInfo, command.getDraw());
        }
    }


    public ExamDto getExam(Long id) {
        ExamDto dto = examMapper.selectById(id);
        List<Long> ids = StringUtil.getIds(dto.getBankIds());
        List<BankDto> selectedBankDtos = bankMapper.selectInBankIds(ids);
        List<BankDto> notSelectedBankDtos = bankMapper.selectNotInBankIds(ids);
        dto.setSelectBankDto(selectedBankDtos);
        dto.setNotSelectBankDto(notSelectedBankDtos);
        return dto;
    }

    public ExamResultDto getExamResult(Long id) {
        ShiroUser user = ShiroUtils.currentUser();
        //如果没登陆
        //TODO 先把验证去掉可以直接进行测试
        //if (user == null) {
        //    throw new ServiceException(ExceptionEnum.LOGIN_INVALID);
        //}
        //如果是老师
        //if (user.getRoleId().longValue() == RoleEnum.TEACHER.getRoleId()) {
        Exam exam = examMapper.selectByPrimaryKey(id);
        if (new Date().getTime() <= exam.getStartTime().getTime()) {
            throw new ServiceException("考试尚未开始没有相关统计信息！");
        }
        ExamResultDto dto = new ExamResultDto();
        //获取参与考试的人员数
        dto.setCounts(examMapper.countStudents(id));
        List<ExerciseDto> exercises = exerciseMapper.selectExerciseByExam(exam.getId());
        //获取模型通过率列表
        List<ModelKillRateDto> modelList = getModelRateList(exercises);
        dto.setKillRates(modelList);
        //获取学生排名分页列表
        List<RankDto> rankList = getRankList(exercises);
        dto.setRanks(rankList);
        return dto;
        //} else {
        //    throw new ServiceException(ExceptionEnum.PERMISSION_DENIED);
        //}
    }


    private List<ModelKillRateDto> getModelRateList(List<ExerciseDto> exercises) {
        //统计每个模型数量的map
        Map<Long, Integer> modelNums = Maps.newHashMap();
        //统计杀死模型数量的map
        Map<Long, Integer> killModelNums = Maps.newHashMap();
        List<ModelKillRateDto> dtos = Lists.newArrayList();
        List<Long> ids = Lists.newArrayList();
        for (ExerciseDto dto : exercises) {
            List<Long> modelIds = StringUtil.getIds(dto.getModelIds());
            List<Long> killModelIds = StringUtil.getIds(dto.getKillModelIds());
            //统计考生所有的model
            for (Long id : modelIds) {
                Integer num = modelNums.get(id) == null || modelNums.get(id) == 0 ? 0 : modelNums.get(id);
                modelNums.put(id, num + 1);
            }
            //统计考生所有kill的model
            for (Long id : killModelIds) {
                Integer num = killModelNums.get(id) == null || killModelNums.get(id) == 0 ? 0 : killModelNums.get(id);
                killModelNums.put(id, num + 1);
            }
        }
        for (Long key : modelNums.keySet()) {
            ids.add(key);
            ModelKillRateDto rateDto = new ModelKillRateDto();
            int modelNum = modelNums.get(key);
            int killModelNum = killModelNums.get(key) == null ? 0 : killModelNums.get(key);
            double rate = killModelNum * 1.0 / modelNum;
            rateDto.setId(key);
            rateDto.setNums(modelNum);
            rateDto.setKilledNums(killModelNum);
            rateDto.setKillRate(rate);
            dtos.add(rateDto);
        }
        if (ids == null || ids.size() == 0) {
            return dtos;
        }
        List<ModelDto> modelDtos = modelMapper.selectByModelIds(ids);
        for (int i = 0; i < modelDtos.size(); i++) {
            dtos.get(i).setModel(modelDtos.get(i));
            dtos.get(i).setName(modelDtos.get(i).getName());
        }
        Collections.sort(dtos, (rate1, rate2) -> compareRate(rate1.getKillRate(), rate2.getKillRate()));
        return dtos;
    }

    private List<RankDto> getRankList(List<ExerciseDto> exerciseDtos) {
        List<RankDto> dtos = Lists.newArrayList();
        //获取每个人的通过率并排序
        for (ExerciseDto dto : exerciseDtos) {
            RankDto rankDto = new RankDto();
            //rankDto.setUserId(dto.getUserId());
            //rankDto.setUsername(dto.getUsername());
            //rankDto.setModelIds(dto.getModelIds());
            //rankDto.setKillModelIds(dto.getKillModelIds());
            BeanUtils.copyProperties(dto, rankDto);
            List<Long> models = StringUtil.getIds(dto.getModelIds());
            List<Long> killModels = StringUtil.getIds(dto.getKillModelIds());
            rankDto.setModelNums(models.size());
            rankDto.setKillModelNums(killModels.size());
            rankDto.setKillRate(rankDto.getKillModelNums() * 1.0 / rankDto.getModelNums());
            dtos.add(rankDto);
        }
        Collections.sort(dtos, (rank1, rank2) -> compareRate(rank1.getKillRate(), rank2.getKillRate()));
        return dtos;
    }

    private int compareRate(double rate1, double rate2) {
        if (rate1 > rate2) {
            return -1;
        } else if (rate1 < rate2) {
            return 1;
        }
        return 0;
    }
}
