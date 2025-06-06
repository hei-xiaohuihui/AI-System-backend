package com.lyh.aiSystem.exception;

import com.lyh.aiSystem.enumeration.ExceptionEnum;

/**
 * @author BigHH
 *  自定义业务异常类
 */
public class BaseException extends RuntimeException{

    // 异常码
    private Integer code;

    // 异常信息
    private String value;

    public BaseException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getValue()); // 将异常消息传递给父类RuntimeException，实现将自定义异常信息输出到Console
        this.code = exceptionEnum.getCode();
        this.value = exceptionEnum.getValue();
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
