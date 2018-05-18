package cn.edu.nju.software.command.shell;

import cn.edu.nju.software.dto.ModelDto;
import lombok.Data;

import java.util.List;

/**
 * 类说明：
 * 创建者：Zeros
 * 创建时间：2018/4/24 下午3:23
 * 包名：cn.edu.nju.software.command.shell
 */

@Data
public class ProcessModelCommand {

    //用户ID
    private Long userId;
    //考试ID
    private Long examId;
    //第几次上传
    private Integer iter;
    //样本文件夹路径
    private String path;
    //模型列表(含模型执行脚本)
    private List<ModelDto> models;
}
