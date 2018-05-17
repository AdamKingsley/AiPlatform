package cn.edu.nju.software.service.shell;

import cn.edu.nju.software.command.shell.ProcessModelCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.util.ShellUtil;
import cn.edu.nju.software.util.StringUtil;
import com.alibaba.druid.support.json.JSONUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：执行测试脚本，检验是否杀死bug
 * 创建者：Zeros
 * 创建时间：2018/4/24 下午1:32
 * 包名：cn.edu.nju.software.service.shell
 */

@Service
public class ProcessModelService {


    public Result processModel(ProcessModelCommand command){


        int count = 0;

        //获取全局路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String projectPath = StringUtil.getParentPath(path,3);

        //解析样本文件夹，获取对应样本路径集合
        String sampleDir = projectPath + command.getPath();
        List<String> sampleList = new ArrayList<>();

        File dirFile = new File(sampleDir);
        File[] files = dirFile.listFiles();
        if(files == null || files.length == 0){
            return Result.error().message("对应文件夹样本为空。");
        }
        for(File file : files){
            if(file == null || file.isDirectory()){
                continue;
            }
            sampleList.add(file.getAbsolutePath());
        }
        System.out.println(path + "python/argparse.py");

        //对于每个模型单独跑一遍正常模型和该变异模型
        List<ModelDto> modelList = command.getModels();

        for(ModelDto model : modelList){
            //依次运行模型或变异模型
            String[] args = {
                    "python",
                    path + "python/argparse.py",
                    "--user_id=" + command.getUserId(),
                    "--exam_id=" + command.getExamId(),
                    "--module_location=" + model.getLocation(),
                    "--script_file=" + model.getScriptLocation(),
                    "--sample_list=" + JSONUtils.toJSONString(sampleList)
            };
            Result result = ShellUtil.exec(args);
            if(result.isSuccess()){
                count++;
            }
        }
        return Result.success().withData(count);
    }

    public Result fgmPicture(){
        return Result.success();
    }
}
