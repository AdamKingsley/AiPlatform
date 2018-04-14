package cn.edu.nju.software.controller;

import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.upload.UploadConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by mengf on 2018/4/12 0012.
 */

@RestController
@RequestMapping("upload")
@EnableConfigurationProperties({UploadConfig.class})
@Slf4j
public class FileUploadController {
    @Autowired

    private UploadConfig uploadConfig;

    @PostMapping("/video")
    public Result uploadVideo(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Result result = checkFile(file);
        if (!result.isSuccess()) {
            return result;
        }
        String path = makeDirs("video");
        log.info("the upload path is {}", path);
        //String originalFilename = file.getOriginalFilename();
        String extension = getExtension(file.getContentType());
        try {
            file.transferTo(new File(path + File.separator + System.currentTimeMillis() + extension));
            return Result.success().message("视频上传成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error().message("视频上传发生异常！");
        }

    }

    @PostMapping("/image/content")
    public Result uploadContentImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Result result = checkFile(file);
        if (!result.isSuccess()) {
            return result;
        }
        makeDirs("content");

        return Result.success().message("图片上传成功！");
    }

    @PostMapping("/image/style")
    public Result uploadStyleImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Result result = checkFile(file);
        if (!result.isSuccess()) {
            return result;
        }
        makeDirs("style");

        return Result.success().message("风格图片上传成功！");
    }

    /**
     * 若保存文件的目录不存在就默认建立对应的文件夹
     *
     * @param folerName
     * @return
     */
    private String makeDirs(String folerName) {
        File targetFile = new File(uploadConfig.getFolder() + folerName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        return targetFile.getAbsolutePath();
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
    private String getExtension(String contenType) {
        try {
            return MimeTypes.getDefaultMimeTypes().
                    forName(contenType).getExtension();
        } catch (MimeTypeException e) {
            e.printStackTrace();
            return "";
        }

    }
}
