package cn.edu.nju.software.util;

/**
 * Created by mengf on 2018/4/11 0011.
 */
public class PasswordHelper {
    public static final String HASH_ALGORITHM = "MD5";
    public static final int ITERATION_TIMES = 1024;
    public static final boolean useHexDecode = true;
    public final static int SALT_LENGTH = 6;
}
