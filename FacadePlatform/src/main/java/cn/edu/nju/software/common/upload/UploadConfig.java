package cn.edu.nju.software.common.upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mengf on 2018/4/12 0012.
 */
@Configuration
@ConfigurationProperties(prefix = "upload")
@Getter
@Setter
public class UploadConfig {
    //上传存到的目录
    private String folder;
    private String root;
}
