package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.command.shell.ProcessModelCommand;
import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.upload.UploadConfig;
import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.dto.SampleDto;
import cn.edu.nju.software.entity.Exam;
import cn.edu.nju.software.entity.Exercise;
import cn.edu.nju.software.mapper.*;
import cn.edu.nju.software.service.shell.ProcessModelService;
import cn.edu.nju.software.util.FileUtil;
import cn.edu.nju.software.util.RandomUtil;
import cn.edu.nju.software.util.StringUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
@Slf4j
public class ExerciseService {


    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExamMapper examMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UploadConfig uploadConfig;
    @Autowired
    private SampleMapper sampleMapper;
    @Autowired
    private ModelProcessMapper modelProcessMapper;
    @Autowired
    private ProcessModelService processService;

    //学生参加过的考试
    //未参加的考试
    //结束的考试
    public ExerciseDto takeExam(Long userId, Long examId) {
        ExerciseDto dto = new ExerciseDto();
        Exercise exercise = new Exercise();
        exercise.setUserId(userId);
        exercise.setExamId(examId);
        BeanUtils.copyProperties(exercise, dto);
        //获取考试信息
        Exam exam = examMapper.selectByPrimaryKey(examId);
        //如果已经结束考试
        if (exam.getEndTime().getTime() <= new Date().getTime()) {
            throw new ServiceException("当前考试已经结束，无法进入！");
        }
        List<Exercise> exercises = exerciseMapper.select(exercise);
        if (exercises != null && exercises.size() > 0) {
            //之前参加了的考试还没结束
            exercise = exercises.get(0);
            BeanUtils.copyProperties(exercise, dto);
            List<Long> modelIds = StringUtil.getIds(dto.getModelIds());
            List<ModelDto> modelDtos = modelMapper.selectByModelIds(modelIds);
            //设置获取到的modelDto的isKilled属性
            if (StringUtils.isEmpty(exercise.getKillModelIds())) {
                modelDtos.forEach(model -> model.setIsKilled(false));
            } else {
                List<Long> killedModelIds = StringUtil.getIds(exercise.getModelIds());
                for (ModelDto modelDto : modelDtos) {
                    if (killedModelIds.contains(modelDto.getId().longValue())) {
                        modelDto.setIsKilled(true);
                    }
                }
            }
            dto.setModelDtos(modelDtos);
        } else {
            //第一次参加该考试要新建exercise对象
            //首先要从题库中选择模型出来
            List<Long> bankIds = StringUtil.getIds(exam.getBankIds());
            List<ModelDto> total_modelDtos = modelMapper.selectByBankIds(bankIds);
            List<ModelDto> exercise_models = getRandomModels(total_modelDtos, exam.getModelNums());
            //未被杀死 init
            exercise_models.forEach(model -> model.setIsKilled(false));
            //将模型添加到exerciseDto
            dto.setModelDtos(exercise_models);
            //设置基础信息
            dto.setStartTime(new Date());
            dto.setKillNums(0);
            dto.setTotalIters(0);
            dto.setUserId(userId);
            //设置题库ID们
            dto.setModelIds(getIdsStr(exercise_models));
            BeanUtils.copyProperties(dto, exercise);
            exerciseMapper.insert(exercise);
        }
        return dto;
    }

    public ExerciseDto uploadSample(Long userId, Long examId, List<MultipartFile> files) {
        if (files.size() == 0) {
            throw new ServiceException("上传的样本为空！");
        }
        Exam exam = examMapper.selectByPrimaryKey(examId);
        Exercise exercise = exerciseMapper.selectByUserAndExam(userId, examId);
        //超过了最大允许上传的次数
        if (exercise.getTotalIters() >= exam.getMaxIters()) {
            throw new ServiceException(ExceptionEnum.ITERS_OUT_LIMIT);
        }

        /*----------创建本次在线运行该用户在该考试下对应的目录，用来存储上传的筛选后的样本--------------*/
        String uploadPath = "exercise/" + userId + "/" + examId + "/" + (exercise.getTotalIters() + 1);
        File dir = FileUtil.makeDirs(uploadConfig.getFolder(), uploadPath);
        FileUtil.clearFiles(dir);
        /*------------------*/

        //判断上传的文件是压缩文件还是多个样本文件(图片为例子)
        MultipartFile file = files.get(0);
        String extension = FileUtil.getExtension(file.getOriginalFilename(), file.getContentType());
        if (files.size() == 1 && (extension.equals(".zip") || extension.equals("rar"))) {
            try {
                //上传压缩文件的测试集
                if (extension.equals(".zip")) {
                    unzip(file, dir, exam.getMaxItems());
                } else {
                    unrar(file, dir, exam.getMaxItems());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //上传多个文件的测试集
            if (files.size() > exam.getMaxItems()) {
                throw new ServiceException(ExceptionEnum.ITEMS_OUT_LIMIT);
            } else {
                try {
                    for (MultipartFile multipartFile : files) {
                        String ab_path = dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename();
                        multipartFile.transferTo(new File(ab_path));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //将exercise的迭代次数上升1
        exercise.setTotalIters(exercise.getTotalIters() + 1);
        exerciseMapper.updateByPrimaryKey(exercise);

        List<Long> modelIds = StringUtil.getIds(exercise.getModelIds());
        //是否删除被kill掉的models
        //List<Long> killModelIds = StringUtil.getIds(exercise.getKillModelIds());
        List<ModelDto> modelDtos = modelMapper.selectByModelIds(modelIds);

        // 所有modelId以及对应的模型的位置 需要到数据库查询 ！！！！
        //modelDtos
        ProcessModelCommand command = new ProcessModelCommand();
        command.setExamId(examId);
        command.setUserId(userId);
        command.setModels(modelDtos);
        command.setIter(exercise.getTotalIters());
        log.info("the upload samples folder is {}", dir.getPath());
        command.setPath(FileUtil.getPath(dir));
        //上传样本运行模型执行情况
        //TODO 异步 需要改进
        processService.processModel(command);
        //执行完 查询通过的model 修改exercise表
        List<Long> killedModelIds = modelProcessMapper.selectKilledModelIds(userId, examId);
        String killedIdStr = StringUtil.getIdsStr(killedModelIds);
        exercise.setKillModelIds(killedIdStr);
        exerciseMapper.updateByPrimaryKey(exercise);
        ExerciseDto dto = new ExerciseDto();
        BeanUtils.copyProperties(exercise, dto);
        //返回dto对象 主要信息在于kill的id
        return dto;
    }


    private void unzip(MultipartFile file, File storeDir, int maxItems) throws IOException {
        Long times = System.currentTimeMillis();
        File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "temp");
        File tempFile = new File(dir.getAbsolutePath() + File.separator + times + ".zip");
        file.transferTo(tempFile);
        //存到temp位置后开始解压
        ZipFile zipFile = new ZipFile(tempFile, Charset.forName("utf-8"));
        int size = zipFile.size();
        for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory() && entry.getName().equals("__MACOSX")) {
                size--;
            }
        }
        if (size > maxItems) {
            log.debug("zip 文件中文件数量超过考试需求");
            throw new ServiceException(ExceptionEnum.ITEMS_OUT_LIMIT);
        }
        for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory() && entry.getName().equals("__MACOSX")) {
                continue;
            }
            String zipEntryName = entry.getName();
            InputStream in = zipFile.getInputStream(entry);
            String ab_path = storeDir.getAbsolutePath() + File.separator + zipEntryName;
            FileOutputStream out = new FileOutputStream(ab_path);
            byte[] buf1 = new byte[1024 * 8];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        log.debug("解压文件完毕，接下来删除缓存的zip文件");
        tempFile.delete();
    }

    private void unrar(MultipartFile file, File storeDir, int maxItem) throws IOException {
        log.debug("暂不支持rar文件上传");
        throw new ServiceException("暂不支持rar文件");
//        Long times = System.currentTimeMillis();
//        File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "temp");
//        File tempFile = new File(dir.getAbsolutePath() + File.separator + times + ".rar");
//        file.transferTo(tempFile);
//        //存到temp位置后开始解压
//        //TODO RAR 目前没有找到良好的解压代码  可以说目前只支持解压ZIP
//        tempFile.delete();
    }

    private List<ModelDto> getRandomModels(List<ModelDto> modelDtos, int num) {
        List<ModelDto> result = Lists.newArrayList();
        //模型数量不足的话 就将模型全部都用上
        if (modelDtos.size() <= num) {
            result.addAll(modelDtos);
            return result;
        }
        //模型数量充足的话 随机选择不重复的num个
        int exclude_num = modelDtos.size() - num;
        if (exclude_num >= num) {
            //如果选少量的话直接选
            int[] array = RandomUtil.randomArray(0, modelDtos.size() - 1, num);
            for (int i : array) {
                result.add(modelDtos.get(i));
            }
            return result;
        } else {
            //如果选比较大量的话直接剔除
            int[] array = RandomUtil.randomArray(0, modelDtos.size() - 1, exclude_num);
            List<Integer> excludeList = Lists.newArrayList();
            for (int i : array) {
                excludeList.add(i);
            }
            for (int i = 0; i < modelDtos.size(); i++) {
                if (!excludeList.contains(i)) {
                    result.add(modelDtos.get(i));
                }
            }
            return result;
        }
    }

    private String getIdsStr(List<ModelDto> models) {
        List<Long> list = Lists.newArrayList();
        models.forEach(model -> list.add(model.getId()));
        String result = StringUtil.getIdsStr(list);
        return result;
    }


    public void downloadReferenceSamples(Long examId, HttpServletResponse response) {
        Exam exam = examMapper.selectByPrimaryKey(examId);
        List<Long> bankIds = StringUtil.getIds(exam.getBankIds());
        List<SampleDto> samples = sampleMapper.selectByBankIds(bankIds);
        List<File> files = Lists.newArrayList();
        for (SampleDto dto : samples) {
            files.add(new File(dto.getLocation()));
        }
        if (files.size() == 1) {
            FileUtil.downloadFile(files.get(0), files.get(0).getName(), response);
        } else {
            FileUtil.downloadZip(files, "samples.zip", response);
        }
    }
}
