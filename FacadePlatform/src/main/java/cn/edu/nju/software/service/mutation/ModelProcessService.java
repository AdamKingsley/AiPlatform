package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.mutation.ProcessPaginationCommand;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.dto.ModelProcessDto;
import cn.edu.nju.software.entity.ModelProcess;
import cn.edu.nju.software.mapper.ModelProcessMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by mengf on 2018/5/7 0007.
 */
@Service
public class ModelProcessService {

    @Autowired
    private ModelProcessMapper mapper;


    public int getIters(Long userId, Long examId, Long modelId) {
        Integer iters = mapper.selectMaxIter(userId, examId, modelId);
        return iters == null ? 0 : iters;
    }

    public ModelProcessDto getProcessDetail(Long userId, Long examId, Long modelId, Integer iter) {
        ModelProcessDto dto = mapper.selectByIter(userId,examId,modelId,iter);
        if (dto==null){
            throw new ServiceException("未获取到该模型在该次提交运行的信息，或尚未运行结束！");
        }
        return dto;
    }

}
