package cn.edu.nju.software.common.exception;

import org.omg.CORBA.UNKNOWN;

/**
 * Created by mengf on 2018/4/6 0006.
 */
public enum ExceptionEnum {

    //错误可以拓展
    UNKNOW_ERROR(-1, "未知错误!"),
    TOKEN_WRONG(-101, "token错误!"),
    PERMISSION_DENIED(-102, "用户没有该权限!"),
    LOGIN_FAILED(-103, "用户登录失败!"),
    REGISTER_FAILED(-104, "用户注册失败!"),
    ACTIVE_DUPLICATED(-105, "用户已经激活过了，请登录！"),
    UNKNOWN_USER(106, "用户不存在！"),
    LOGIN_INVALID(-107,"登录失效请重登！"),
    USER_BLOCKED(-108,"用户账户被冻结，登录失败，请联系管理员！"),
    USER_NOT_ACTIVE(-109,"用户账户还未激活，请查收激活邮件，若未收到请联系管理员！"),
    ITEMS_OUT_LIMIT(301, "上传样本数量超过考试限制！"),
    ITERS_OUT_LIMIT(302, "上传样本次数超过考试限制！");

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