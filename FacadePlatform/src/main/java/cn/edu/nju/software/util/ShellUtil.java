package cn.edu.nju.software.util;

import cn.edu.nju.software.common.result.Result;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 类说明：执行脚本工具
 * 创建者：Zeros
 * 创建时间：2018/4/20 下午4:34
 * 包名：cn.edu.nju.software.util
 */

public class ShellUtil {

    /**
     * 调用shell脚本的通用方法
     * @param args
     * @return
     */
    public static Result exec(String[] args) {
        try {
            Process ps = Runtime.getRuntime().exec(args);
            ps.waitFor();

            String normal = getString(ps.getInputStream());
            String error = getString(ps.getErrorStream());
            System.out.println("normal input for script: \n"+normal);
            System.out.println("error input for script: \n"+error);
            if((normal == null || normal.equals("")) && error != null ){
                return Result.error().message("脚本执行失败");
            }
            return Result.success().withData(normal);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("脚本执行失败");
        }
    }

    private static String getString(InputStream inputStream){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
