package cn.edu.nju.software.util;

import cn.edu.nju.software.common.result.Result;
import com.google.common.collect.Lists;
import com.sun.imageio.plugins.common.ImageUtil;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by mengf on 2018/4/27 0027.
 */
public class FileUtil {


    /**
     * 若保存文件的目录不存在就默认建立对应的文件夹
     *
     * @param folerName
     * @return
     */
    public static File makeDirs(String folder, String folerName) {
        File targetFile = new File(folder + folerName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        //log.info("the path is : {}", targetFile.getPath());
        //log.info("the absolute path is : {}", targetFile.getAbsolutePath());
        return targetFile;
    }

    /**
     * 检查文件是否为空
     *
     * @param file
     * @return
     */
    public static Result checkFile(MultipartFile file) {
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
    public static String getExtension(String filename, String contenType) {
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

    public static void clearFiles(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
}
