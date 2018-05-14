package cn.edu.nju.software.util;

import cn.edu.nju.software.common.result.Result;
import com.google.common.collect.Lists;
import com.sun.imageio.plugins.common.ImageUtil;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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


    public static void downloadFile(File file, String fileName, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try {
            InputStream myStream = new FileInputStream(file);
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void downloadZip(List<File> files, String filename, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
        } catch (Exception e) {
            e.printStackTrace();
        }
        //循环将文件写入压缩流
        DataOutputStream os = null;
        try {
            for (File file : files) {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                zipos.putNextEntry(new ZipEntry(file.getName()));
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length = 0;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                is.close();
                zipos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                os.flush();
                os.close();
                zipos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
