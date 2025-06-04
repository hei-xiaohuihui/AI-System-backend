package com.lyh.aiSystem.handler;

import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author BigHH
 *  全局异常处理类
 */
@RestControllerAdvice // 包含@ControllerAdvice注解和@ResponseBody注解
public class GlobalExceptionHandler{

    /**
     *  处理自定义全局业务异常：将BaseException的异常码和异常信息封装成Result对象返回给前端
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result businessExceptionHandler(BaseException ex) {
        return Result.error(ex.getCode(), ex.getValue());
    }

    /**
     *  处理其他异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result OthersExceptionHandler(Exception ex) {
        ex.printStackTrace();
        return Result.error(ExceptionEnum.SYSTEM_ERROR.getCode(), ExceptionEnum.SYSTEM_ERROR.getValue());
    }
}
