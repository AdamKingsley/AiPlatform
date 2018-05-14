package cn.edu.nju.software.util;

import cn.edu.nju.software.common.result.Result;

import java.io.BufferedReader;
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

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
            return Result.success().withData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("脚本执行失败");
        }
    }
}
