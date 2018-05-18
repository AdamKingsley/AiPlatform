package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.mutation.ProcessPaginationCommand;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.dto.ModelProcessDto;
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

    //TODO
    public int getIters(Long examId, Long modelId) {
        return 0;
    }

    //TODO
    public List<ModelProcessDto> getAll(Long userId, Long examId, Long modelId) {
        return Lists.newArrayList();
    }

    //TODO
    public PageResult list(Long userId, Long examId, Long modelId, ProcessPaginationCommand command) {
        return null;
    }

    //TODO
    public Object getProcessDetail(Long id) {
        try {
            JsonNode node = new ObjectMapper().readTree(new File(".."));

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("读取模型在线测试文件出错！");
        }
        return null;
    }
}
