package cn.edu.nju.software.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mengf on 2018/5/8 0008.
 */
@Getter
@Setter
public class ModelProcessDto {
    private Long id;
    //变异模型ID
    private Long modelId;
    //考试ID
    private Long examId;
    //用户ID
    private Long userId;
    //开始运行这模型的时间
    private Date processTime;
    //是否杀死该变异模型
    private Boolean isKilled;
    //测试样本位置一些
    private String samples;
    //模型处理测试过程中的一些中间结果的位置
    //供参加考试的用户查看自己的处理的中间结果等
    private String resultLocation;
}
