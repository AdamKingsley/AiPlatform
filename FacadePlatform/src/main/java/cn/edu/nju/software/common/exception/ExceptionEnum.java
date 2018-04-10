package cn.edu.nju.software.common.exception;

/**
 * Created by mengf on 2018/4/6 0006.
 */
public enum ExceptionEnum {

    //错误可以拓展
    UNKNOW_ERROR(-1, "未知错误"),
    TOKEN_WRONG(-101, "token错误"),
    USER_NOT_FIND(-102, "用户登录失败"),
    USER_REGISTER_FAILED(-103, "用户注册失败");

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}