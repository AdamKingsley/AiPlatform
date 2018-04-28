package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.mutation.ModelCommand;
import cn.edu.nju.software.command.mutation.ModelPaginationCommand;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageInfo;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.entity.Model;
import cn.edu.nju.software.mapper.BankMapper;
import cn.edu.nju.software.mapper.ModelMapper;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by mengf on 2018/4/26 0026.
 */
@Service
public class ModelService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BankMapper bankMapper;

    public void delete(List<Long> ids) {
        //删除模型的同时减少对应题库中的数量
        Example example = new Example(Model.class);
        example.createCriteria().andIn("id",ids);
        List<Model> models = modelMapper.selectByExample(example);
        Map<Long,List<Long>> modelsList = devidedByBankId(models);
        modelsList.forEach((bankId,modelIds)->{
            example.createCriteria().andIn("id",modelIds);
            modelMapper.deleteByExample(example);
            bankMapper.subModelNums(modelIds.size());
        });
    }

    //将model们按照bankid分开
    private  Map<Long,List<Long>> devidedByBankId(Iterable<Model> models){
        Map<Long,List<Long>> map = Maps.newHashMap();
        for(Model model : models){
            if (map.get(model.getBank_id().longValue())==null){
                map.put(model.getBank_id(), Lists.newArrayList());
            }
            map.get(model.getBank_id().longValue()).add(model.getId());
        }
        return map;
    }

    public void update(ModelCommand command) {
        Model model = modelMapper.selectByPrimaryKey(command.getId());
        if (command.getName().equals(model.getName())){
            int num = modelMapper.countByName(model.getName(),model.getBank_id());
            if (num>0){
                throw new ServiceException("该名称已存在！");
            }
        }
        BeanUtils.copyProperties(command,model);
        modelMapper.updateByPrimaryKey(model);
    }

    public PageResult list(ModelPaginationCommand command) {

        Page<ModelDto> page =  modelMapper.selectModelPage(command);
        return new PageResult(new PageInfo(page));
    }

    public void download(Long id, HttpServletResponse response) {
        Model model = modelMapper.selectByPrimaryKey(id);
        String modelPath = model.getLocation();
        if (StringUtils.isEmpty(modelPath)) {
            throw new ServiceException("不存在对应的模型文件！");
        }
        File file = new File(modelPath);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        try {
            InputStream myStream = new FileInputStream(file);
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
