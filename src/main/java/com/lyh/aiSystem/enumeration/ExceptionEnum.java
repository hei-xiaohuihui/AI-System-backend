package com.lyh.aiSystem.enumeration;


/**
 *  异常枚举类
 */
public enum ExceptionEnum {

    /**
     *  系统角色相关异常
     */
    /*
     *  普通用户相关异常
     */
    USER_ALREADY_EXIST(11001, "用户已存在"),
    USER_NOT_EXIST(11002, "用户不存在"),
    USER_USERNAME_OR_PASSWORD_ERROR(11003, "用户名或密码错误"),
    USER_TOKEN_MISSING(11004, "Missing Token"),
    USER_TOKEN_EXPIRED(11005, "Token has expired"),
    USER_TOKEN_INVALID(11006, "Invalid Token"),
    USER_NOT_LOGIN(11007, "用户未登录"),

    /*
     *  管理员相关异常
     */
    ADMIN_NOT_EXIST(12001, "管理员不存在"),

    /**
     *  数据库相关异常
     */
    DB_INSERT_ERROR(20001, "DB插入失败"),
    DB_UPDATE_ERROR(20002, "DB更新失败"),

    /**
     *  聊天相关异常
     */
    CHAT_SESSION_NOT_EXIST(30001, "会话不存在"),

    /**
     *  系统异常：用于在通用系统错误时抛出
     */
    SYSTEM_ERROR(90001, "系统异常");

    // 异常码
    private Integer code;

    // 异常信息
    private String value;

    ExceptionEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
