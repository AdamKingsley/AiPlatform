package cn.edu.nju.software.service.shell;

import cn.edu.nju.software.command.shell.ProcessModelCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.ModelDto;
import cn.edu.nju.software.util.ShellUtil;
import com.alibaba.druid.support.json.JSONUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类说明：执行测试脚本，检验是否杀死bug
 * 创建者：Zeros
 * 创建时间：2018/4/24 下午1:32
 * 包名：cn.edu.nju.software.service.shell
 */

@Service
public class ProcessModelService {

    private String path;

    public Result processModel(ProcessModelCommand command){
        List<ModelDto> modelList = command.getModels();
        int count = 0;
        for(ModelDto model : modelList){
            String[] args = {
                    "python",
                    path + "/argparse.py",
                    "--user_id=" + command.getUserId(),
                    "--exam_id=" + command.getExamId(),
//                    "--script_file=" + model.getLocation(),
//                    "--sample_list=" + JSONUtils.toJSONString(command.getSamples())
            };
            Result result = ShellUtil.exec(args);
            if(result.isSuccess()){
                count++;
            }
        }
        return Result.success().withData(count);
    }
}
