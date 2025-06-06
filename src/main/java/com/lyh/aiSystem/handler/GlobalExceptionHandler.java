package com.lyh.aiSystem.handler;

import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.utils.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

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
    public ResponseEntity<Result> businessExceptionHandler(BaseException ex) {
//        return Result.error(ex.getCode(), ex.getValue());

        // 如果是token相关错误，返回401状态码
        if(Objects.equals(ex.getCode(), ExceptionEnum.USER_TOKEN_EXPIRED.getCode())
                || Objects.equals(ex.getCode(), ExceptionEnum.USER_TOKEN_MISSING.getCode())
                || Objects.equals(ex.getCode(), ExceptionEnum.USER_TOKEN_INVALID.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error(ex.getCode(), ex.getValue()));
        }

        // 其他业务异常：返回400状态码
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 设置状态码为400
                .body(Result.error(ex.getCode(), ex.getValue())); // 封装自定义错误信息并返回给前端
    }

    /**
     *  处理其他异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> OthersExceptionHandler(Exception ex) {
        ex.printStackTrace();
//        return Result.error(ExceptionEnum.SYSTEM_ERROR.getCode(), ExceptionEnum.SYSTEM_ERROR.getValue());
        // 其他异常默认返回500状态码
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 设置状态码为500
                .body(Result.error(ExceptionEnum.SYSTEM_ERROR.getCode(), ExceptionEnum.SYSTEM_ERROR.getValue())); // 设置自定义错误信息
    }
}
