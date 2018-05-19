package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.mutation.BankCommand;
import cn.edu.nju.software.command.mutation.BankPaginationCommand;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageInfo;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.upload.UploadConfig;
import cn.edu.nju.software.dto.BankDto;
import cn.edu.nju.software.entity.Bank;
import cn.edu.nju.software.entity.Model;
import cn.edu.nju.software.entity.Sample;
import cn.edu.nju.software.mapper.BankMapper;
import cn.edu.nju.software.mapper.ModelMapper;
import cn.edu.nju.software.mapper.SampleMapper;
import cn.edu.nju.software.util.FileUtil;
import cn.edu.nju.software.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by mengf on 2018/4/19 0019.
 */
@Service
@Slf4j
public class BankService {

    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private UploadConfig uploadConfig;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SampleMapper sampleMapper;

    public void update(BankCommand command) {
        Bank bank = bankMapper.selectByPrimaryKey(command.getId());
        //如果修改了题库的名称
        if (!bank.getName().equals(command.getName())) {
            int num = bankMapper.countByName(command.getName());
            if (num > 0) {
                throw new ServiceException("题库名称已经存在！");
            }
        }
        //正常更新 刷新更新时间
        bank.setModifyTime(new Date());
        BeanUtils.copyProperties(command, bank);
        bankMapper.updateByPrimaryKey(bank);
    }

    public void update(BankCommand command, HttpServletRequest request) {
        Bank bank = bankMapper.selectByPrimaryKey(command.getId());
        //如果修改了题库的名称
        if (!bank.getName().equals(command.getName())) {
            int num = bankMapper.countByName(command.getName());
            if (num > 0) {
                throw new ServiceException("题库名称已经存在！");
            }
        }
        //正常更新 刷新更新时间
        bank.setModifyTime(new Date());
        BeanUtils.copyProperties(command, bank);
        bankMapper.updateByPrimaryKey(bank);
        uploadFiles(bank.getId(), request);
    }


    public void create(BankCommand command) {
        Bank bank = new Bank();
        BeanUtils.copyProperties(command, bank);
        bank.setCreateTime(new Date());
        bank.setModifyTime(bank.getCreateTime());
        bank.setNums(0);
        bankMapper.insert(bank);
    }

    public void create(BankCommand command, HttpServletRequest request) {
        Bank bank = new Bank();
        BeanUtils.copyProperties(command, bank);
        bank.setCreateTime(new Date());
        bank.setModifyTime(bank.getCreateTime());
        bank.setNums(0);
        bank.setDeleted(false);
        bankMapper.insert(bank);
        uploadFiles(bank.getId(), request);
    }

    private void uploadFiles(Long id, HttpServletRequest request) {
        MultipartFile script = ((MultipartHttpServletRequest) request).getFile("script");
        MultipartFile standardModel = ((MultipartHttpServletRequest) request).getFile("standardModel");
        List<MultipartFile> models = ((MultipartHttpServletRequest) request).getFiles("models");
        List<MultipartFile> samples = ((MultipartHttpServletRequest) request).getFiles("samples");
        if (script != null && script.getSize() != 0) {
            uploadScript(id, script);
        }
        if (standardModel != null && standardModel.getSize() != 0) {
            uploadStandardModel(id, standardModel);
        }
        if (models != null && models.size() > 0) {
            uploadMutationModel(id, models);
        }
        if (samples != null && samples.size() > 0) {
            uploadSamples(id, samples);
        }
    }

    //获取题库列表 分页
    public PageResult list(BankPaginationCommand command) {
        PageHelper.startPage(command.getPageNum(), command.getPageSize());
        Page<BankDto> page = bankMapper.selectPage();
        PageInfo<BankDto> pageInfo = new PageInfo(page);
        return new PageResult(pageInfo, command.getDraw());
    }

    public void delete(List<Long> ids) {
        //不仅要删除题库信息还要删除模型信息
        for (Long id : ids) {
            bankMapper.deleteByPrimaryKey(id);
            modelMapper.deleteByPrimaryKey(id);
            //TODO 删除本地存储script/model/samples文件 防止浪费空间
        }
    }


    public BankDto getBankDetail(Long id) {
        BankDto dto = bankMapper.selectById(id);
        return dto;
    }

    public void downloadScript(Long id, HttpServletResponse response) {
        Bank bank = bankMapper.selectByPrimaryKey(id);
        String scriptPath = bank.getScriptLocation();
        if (StringUtils.isEmpty(scriptPath)) {
            throw new ServiceException("不存在对应的脚本文件！");
        }
        File file = new File(scriptPath);
        FileUtil.downloadFile(file, file.getName(), response);
    }


    public void downloadModels(Long id, HttpServletResponse response) {
        Model modelTemplate = new Model();
        modelTemplate.setBankId(id);
        List<Model> models = modelMapper.select(modelTemplate);
        if (models == null || models.size() == 0) {
            throw new ServiceException("尚未上传模型，模型文件为空！");
        }
        List<File> files = Lists.newArrayList();
        for (Model model : models) {
            File file = new File(model.getLocation());
            files.add(file);
        }
        if (files.size() == 1) {
            FileUtil.downloadFile(files.get(0), files.get(0).getName(), response);
        } else {
            FileUtil.downloadZip(files, "models.zip", response);
        }

    }

    public Result uploadScript(Long id, MultipartFile file) {
        Result result = FileUtil.checkFile(file);
        if (!result.isSuccess()) {
            return result;
        }
        File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "bank_" + id + "/");
        //String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.getExtension(file.getOriginalFilename(), file.getContentType());
        String ab_path = dir.getAbsolutePath() + File.separator + "script" + extension;
        String path = dir.getPath() + File.separator + "script" + extension;
        try {
            file.transferTo(new File(ab_path));
            //return Result.success().message("执行脚本上传成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error().message("脚本上传发生异常！");
        }
        Bank bank = bankMapper.selectByPrimaryKey(id);
        bank.setScriptLocation(FileUtil.getPath(path));
        bankMapper.updateByPrimaryKey(bank);
        return Result.success().message("上传脚本成功！");
    }

    public Result uploadMutationModel(Long id, List<MultipartFile> models) {
        Bank bank = bankMapper.selectByPrimaryKey(id);
        int nums = bank.getNums();
        if (models.size() == 0) {
            return Result.error().errorMessage("上传模型数量为0！");
        }
        for (MultipartFile file : models) {
            File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "bank_" + id + "/models/");
            //String originalFilename = file.getOriginalFilename();
            String extension = FileUtil.getExtension(file.getOriginalFilename(), file.getContentType());
            nums += 1;
            String ab_path = dir.getAbsolutePath() + File.separator + "model_" + nums + extension;
            String path = dir.getPath() + File.separator + "model_" + nums + extension;
            try {
                file.transferTo(new File(ab_path));
                //return Result.success().message("执行脚本上传成功！");
                Model model = new Model();
                model.setName("model_" + nums);
                model.setCreateTime(new Date());
                model.setModifyTime(model.getCreateTime());
                model.setBankId(id);
                model.setLocation(FileUtil.getPath(path));
                //model.setType(1);
                modelMapper.insert(model);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error().message("模型上传发生异常！");
            }
        }
        bank.setNums(bank.getNums() + models.size());
        bank.setModifyTime(new Date());
        bankMapper.updateByPrimaryKey(bank);
        return Result.success().message("上传模型到题库成功！");
    }


    public void downloadSamples(Long id, HttpServletResponse response) {
        Sample sampleTemplate = new Sample();
        sampleTemplate.setBankId(id);
        List<Sample> samples = sampleMapper.select(sampleTemplate);
        if (samples == null || samples.size() == 0) {
            throw new ServiceException("尚未上传样本集文件，参考测试样本集为空！");
        }
        List<File> files = Lists.newArrayList();
        for (Sample sample : samples) {
            File file = new File(sample.getLocation());
            files.add(file);
        }
        if (files.size() == 1) {
            FileUtil.downloadFile(files.get(0), files.get(0).getName(), response);
        } else {
            FileUtil.downloadZip(files, "samples.zip", response);
        }
    }

    public Result uploadSamples(Long id, List<MultipartFile> files) {
        Long num = sampleMapper.countByBankId(id);

        for (MultipartFile file : files) {
            num += 1;
            File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "bank_" + id + "/samples/");
            //String originalFilename = file.getOriginalFilename();
            String extension = FileUtil.getExtension(file.getOriginalFilename(), file.getContentType());
            String ab_path = dir.getAbsolutePath() + File.separator + "sample_" + num + extension;
            String path = dir.getPath() + File.separator + "sample_" + num + extension;
            try {
                file.transferTo(new File(ab_path));
                Sample sample = new Sample();
                sample.setName("sample_" + num);
                sample.setBankId(id);
                sample.setLocation(FileUtil.getPath(path));
                sampleMapper.insert(sample);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error().message("参考样本集上传发生异常！");
            }
        }
        return Result.success().message("样本集上传到系统成功！");
    }

    public List<BankDto> listAll() {
        return bankMapper.selectAllBanks();
    }

    public void downloadStandardModel(Long id, HttpServletResponse response) {
        Bank bank = bankMapper.selectByPrimaryKey(id);
        String standardModelPath = bank.getStandardModelLocation();
        if (StringUtils.isEmpty(standardModelPath)) {
            throw new ServiceException("不存在对应的模型文件！");
        }
        File file = new File(standardModelPath);
        FileUtil.downloadFile(file, file.getName(), response);
    }

    public Result uploadStandardModel(Long id, MultipartFile file) {
        Result result = FileUtil.checkFile(file);
        if (!result.isSuccess()) {
            return result;
        }
        File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "bank_" + id + "/standard_model/");
        //String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.getExtension(file.getOriginalFilename(), file.getContentType());
        String ab_path = dir.getAbsolutePath() + File.separator + "model" + extension;
        String path = dir.getPath() + File.separator + "model" + extension;
        try {
            file.transferTo(new File(ab_path));
            //return Result.success().message("执行脚本上传成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error().message("标准模型上传发生异常！");
        }
        Bank bank = bankMapper.selectByPrimaryKey(id);
        bank.setStandardModelLocation(FileUtil.getPath(path));
        bankMapper.updateByPrimaryKey(bank);
        return Result.success().message("上传成标准模型功！");
    }
}
