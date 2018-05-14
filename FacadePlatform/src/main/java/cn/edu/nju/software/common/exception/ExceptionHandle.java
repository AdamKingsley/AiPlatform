package cn.edu.nju.software.common.exception;

import cn.edu.nju.software.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.io.UncheckedIOException;

/**
 * Created by mengf on 2018/4/6 0006.
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Result exceptionGet(Exception e) {
        e.printStackTrace();

        //自定义异常 ServiceException
        if (e instanceof ServiceException) {
            ServiceException exception = (ServiceException) e;
            return Result.error().errorCode(exception.getCode().toString()).errorMessage(e.getMessage());
        }
        //shiro相关异常
        if (e instanceof UnauthenticatedException) {
            return Result.error().exception(ExceptionEnum.TOKEN_WRONG);
        }
        if (e instanceof UnauthorizedException) {
            return Result.error().exception(ExceptionEnum.PERMISSION_DENIED);
        }
        if (e instanceof UnknownAccountException || e instanceof IncorrectCredentialsException) {
            return Result.error().exception(ExceptionEnum.LOGIN_FAILED);
        }

        //other异常
        return Result.error().errorCode("-1").message("系统内部异常:" + e.getClass()).errorMessage(e.getMessage());
    }
}
