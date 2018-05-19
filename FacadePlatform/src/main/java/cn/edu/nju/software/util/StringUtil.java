package cn.edu.nju.software.util;

import cn.edu.nju.software.dto.ModelDto;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by mengf on 2018/5/14 0014.
 */
public class StringUtil {
    private static final String SEPERATOR = ",";

    public static List<Long> getIds(String ids) {
        List<Long> idList = Lists.newArrayList();
        if (ids == null || ids.equals("")) {
            return idList;
        }
        String[] idArray = ids.split(SEPERATOR);
        for (String id : idArray) {
            idList.add(Long.parseLong(id));
        }
        return idList;
    }

    public static String getIdsStr(List<Long> list) {
        String result = "";
        for (Long id : list) {
            result += id;
            result += SEPERATOR;
        }
        if (result != "" && result.lastIndexOf(SEPERATOR) == result.length() - 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static Long getLongValue(String str) {
        return Long.parseLong(str);
    }

    /**
     * 获取父文件夹路径
     *
     * @param str   路径
     * @param times 次数
     * @return
     */
    public static String getParentPath(String str, int times) {
        if (times < 1) {
            return str;
        }
        String result = str;

        for (int i = 0; i < times; i++) {
            int index = result.lastIndexOf('/');
            if (index < 0) {
                result = "";
            } else if (index == 0) {
                result = "/";
            } else {
                result = result.substring(0, index);
            }
        }
        return result;
    }

    /**
     * 获取路径中的文件名，含后缀
     */
    public static String getFileFromPath(String str) {
        int index = str.lastIndexOf('/');
        if (index < 0) {
            return str;
        } else {
            return str.substring(index + 1);
        }
    }

    /**
     * 获取路径中的文件名，不含后缀
     */
    public static String getFileName(String str) {
        int index = str.lastIndexOf('.');
        if (index < 0) {
            return str;
        } else {
            return str.substring(0, index);
        }
    }

}
