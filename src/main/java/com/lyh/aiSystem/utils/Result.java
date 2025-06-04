package com.lyh.aiSystem.utils;

import lombok.Data;

/**
 * @author BigHH
 *  统一相应结果封装类
 */
@Data
public class Result<T> {

    // 响应码
    private Integer code;

    // 响应消息
    private String message;

    // 响应数据
    private T data;

    private static final Integer SUCCESS_CODE = 200; // 默认成功装响应码

    private static final String SUCCESS_MESSAGE = "success"; // 默认成功响应消息

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(Integer code, String message) {
        this(code, message, null);
    }

    public Result(T data) {
        this(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public Result() {
        this(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }

    /**
     *  成功响应结果——不传递任何参数
     * @return
     * @param <T>
     */
    public static <T> Result<T> success() {
        return new Result<>();
    }

    /**
     *  成功响应结果——传递响应数据
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    /**
     *  失败响应结果——传递错误码和错误消息
     * @param code
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
}
