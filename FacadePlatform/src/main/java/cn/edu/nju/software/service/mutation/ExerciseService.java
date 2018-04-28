package cn.edu.nju.software.service.mutation;

import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.upload.UploadConfig;
import cn.edu.nju.software.dto.ExerciseDto;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.entity.Exam;
import cn.edu.nju.software.entity.Exercise;
import cn.edu.nju.software.mapper.ExamMapper;
import cn.edu.nju.software.mapper.ExerciseMapper;
import cn.edu.nju.software.mapper.ModelMapper;
import cn.edu.nju.software.util.FileUtil;
import cn.edu.nju.software.util.RandomUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by mengf on 2018/4/20 0020.
 */
@Service
@Slf4j
public class ExerciseService {

    private static final String SEPERATOR = ",";
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExamMapper examMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UploadConfig uploadConfig;

    //学生参加过的考试
    //未参加的考试
    //结束的考试
    public Result takeExam(Long userId, Long examId) {
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
            List<Long> modelIds = getIds(dto.getModelIds());
            List<ModelDto> modelDtos = modelMapper.selectByModelIds(modelIds);
            //设置获取到的modelDto的isKilled属性
            if (StringUtils.isEmpty(exercise.getKillModelIds())) {
                modelDtos.forEach(model -> model.setIsKilled(false));
            } else {
                List<Long> killedModelIds = getIds(exercise.getModelIds());
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
            List<Long> bankIds = getIds(exam.getBankIds());
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
        return Result.success().message("获取学生本场考试信息成功！").withData(dto);
    }

    public Result uploadSample(Long userId, Long examId, List<MultipartFile> files) {
        if (files.size() == 0) {
            throw new ServiceException("上传的样本为空！");
        }
        Exam exam = examMapper.selectByPrimaryKey(examId);
        Exercise exercise = exerciseMapper.selectByUserAndExam(userId, examId);
        //超过了最大允许上传的次数
        if (exercise.getTotalIters() >= exam.getMaxIters()) {
            return Result.error().exception(ExceptionEnum.ITERS_OUT_LIMIT);
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
                return Result.error().exception(ExceptionEnum.ITEMS_OUT_LIMIT);
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
        // TODO 然后异步调用执行脚本接口
        // TODO 参数->
        // TODO userId✔，examId✔，path(存储本次在线运行样本的所有文件的文件夹目录)✔，
        // TODO 所有modelId以及对应的模型的位置 需要到数据库查询 ！！！！
        return Result.success().message("上传测试样本成功，正在执行测试脚本！");
    }

    private void unzip(MultipartFile file, File storeDir, int maxItems) throws IOException {
        Long times = System.currentTimeMillis();
        File dir = FileUtil.makeDirs(uploadConfig.getFolder(), "temp");
        File tempFile = new File(dir.getAbsolutePath() + File.separator + times + ".zip");
        file.transferTo(tempFile);
        //存到temp位置后开始解压
        ZipFile zipFile = new ZipFile(tempFile, Charset.forName("utf-8"));
        if (zipFile.size() > maxItems) {
            log.debug("zip 文件中文件数量超过考试需求");
            throw new ServiceException(ExceptionEnum.ITEMS_OUT_LIMIT);
        }
        for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
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

    private String getIdsStr(Iterable<String> ids) {
        String result = "";
        for (String id : ids) {
            result += id;
            result += SEPERATOR;
        }
        if (result.lastIndexOf(SEPERATOR) == result.length() - 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }


    private String getIdsStr(List<ModelDto> models) {
        String result = "";
        for (ModelDto dto : models) {
            result += dto.getId();
            result += SEPERATOR;
        }
        if (result.lastIndexOf(SEPERATOR) == result.length() - 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private List<Long> getIds(String ids) {
        String[] idArray = ids.split(SEPERATOR);
        List<Long> idList = Lists.newArrayList();
        for (String id : idArray) {
            idList.add(Long.parseLong(id));
        }
        return idList;
    }


}
