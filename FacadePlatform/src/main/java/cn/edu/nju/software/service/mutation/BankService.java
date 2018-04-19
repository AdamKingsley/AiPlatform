package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.mutation.BankCommand;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageInfo;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.upload.UploadConfig;
import cn.edu.nju.software.entity.Bank;
import cn.edu.nju.software.entity.Model;
import cn.edu.nju.software.mapper.BankMapper;
import cn.edu.nju.software.mapper.ModelMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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


    public void update(BankCommand command) {
        Bank bank = bankMapper.selectByPrimaryKey(command.getId());
        bank.setModifyTime(new Date());
        BeanUtils.copyProperties(command, bank);
        bankMapper.updateByPrimaryKey(bank);
    }

    public void create(BankCommand command) {
        Bank bank = new Bank();
        BeanUtils.copyProperties(command, bank);
        bank.setCreateTime(new Date());
        bank.setModifyTime(bank.getCreateTime());
        bank.setNums(0);
        bankMapper.insert(bank);
    }

    //获取题库列表 分页
    public PageResult list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Bank> page = bankMapper.selectPage();
        PageInfo<Bank> pageInfo = new PageInfo(page);
        return new PageResult(pageInfo);
    }

    public void delete(List<Long> ids) {
        //不仅要删除题库信息还要删除模型信息
        for (Long id : ids) {
            bankMapper.deleteByPrimaryKey(id);
            modelMapper.deleteByPrimaryKey(id);
        }
    }

    public void downloadScript(Long id, HttpServletResponse response) {
        Bank bank = bankMapper.selectByPrimaryKey(id);
        String scriptPath = bank.getScriptLocation();
        if (StringUtils.isEmpty(scriptPath)) {
            throw new ServiceException("不存在对应的脚本文件！");
        }
        File file = new File(scriptPath);
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


    public void downloadModels(Long id, HttpServletResponse response) {
        Model modelTemplate = new Model();
        modelTemplate.setBank_id(id);
        List<Model> models = modelMapper.select(modelTemplate);
        if (models == null || models.size() == 0) {
            throw new ServiceException("尚未上传模型，模型文件为空！");
        }
        List<File> files = Lists.newArrayList();
        for (Model model : models) {
            File file = new File(model.getLocation());
            files.add(file);
        }

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=models.zip");
        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
        } catch (Exception e) {
            e.printStackTrace();
        }
        //循环将文件写入压缩流
        DataOutputStream os = null;
        try {
            for (File file : files) {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                zipos.putNextEntry(new ZipEntry(file.getName()));
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length = 0;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                is.close();
                zipos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                os.flush();
                os.close();
                zipos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Result uploadScript(Long id, MultipartFile file) {
        Result result = checkFile(file);
        if (!result.isSuccess()) {
            return result;
        }
        File dir = makeDirs("bank_" + id + "/");
        //String originalFilename = file.getOriginalFilename();
        String extension = getExtension(file.getOriginalFilename(), file.getContentType());
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
        bank.setScriptLocation(path);
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
            File dir = makeDirs("bank_" + id + "/models/");
            //String originalFilename = file.getOriginalFilename();
            String extension = getExtension(file.getOriginalFilename(), file.getContentType());
            nums += 1;
            String ab_path = dir.getAbsolutePath() + File.separator + "model_" + nums + extension;
            String path = dir.getPath() + File.separator + "model_" + nums + extension;
            try {
                file.transferTo(new File(ab_path));
                //return Result.success().message("执行脚本上传成功！");
                Model model = new Model();
                model.setBank_id(id);
                model.setLocation(path);
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


    /**
     * 若保存文件的目录不存在就默认建立对应的文件夹
     *
     * @param folerName
     * @return
     */
    private File makeDirs(String folerName) {
        File targetFile = new File(uploadConfig.getFolder() + folerName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        log.info("the path is : {}", targetFile.getPath());
        log.info("the absolute path is : {}", targetFile.getAbsolutePath());
        return targetFile;
    }

    /**
     * 检查文件是否为空
     *
     * @param file
     * @return
     */
    private Result checkFile(MultipartFile file) {
        if (file.isEmpty()) {
            if (file.isEmpty()) {
                return Result.error().message("文件为空，文件上传失败！");
            }
        }
        return Result.success().message("文件不为空,可继续进行存储操作！");
    }

    /**
     * 获取文件的拓展名 tika库
     *
     * @param contenType
     * @return
     */
    private String getExtension(String filename, String contenType) {
        //如果有后缀名直接用后缀名
        if (filename.lastIndexOf('.') >= 0) {
            return filename.substring(filename.lastIndexOf('.'), filename.length());
        }
        try {
            return MimeTypes.getDefaultMimeTypes().
                    forName(contenType).getExtension();
        } catch (MimeTypeException e) {
            e.printStackTrace();
            return "";
        }
    }

}
