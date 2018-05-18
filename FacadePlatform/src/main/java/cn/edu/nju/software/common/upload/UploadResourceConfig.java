package cn.edu.nju.software.common.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * Created by mengf on 2018/5/18 0018.
 */
@Configuration
@EnableConfigurationProperties({UploadConfig.class})
@Slf4j
public class UploadResourceConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private UploadConfig uploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //log.info("the addResourceHandler is {}", uploadConfig.getFolder() + "**");
        //log.info("the addResourceLocation is {}", new File(uploadConfig.getFolder()).getAbsolutePath());
        registry.addResourceHandler(uploadConfig.getFolder() + "**").addResourceLocations("file:" + new File(uploadConfig.getFolder()).getAbsolutePath() + File.separator);
        super.addResourceHandlers(registry);
    }
}
