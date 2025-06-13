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
    USER_ID_GET_FAIL(11008, "用户id获取失败"),

    /*
     *  管理员相关异常
     */
    ADMIN_NOT_EXIST(12001, "管理员不存在"),
    ADMIN_USERNAME_OR_PASSWORD_ERROR(12002, "用户名或密码错误"),
    ADMIN_NOT_SUPPER_ADMIN(12003, "非超级管理员用户"),
    ADMIN_ALREADY_EXIST(12004, "管理员/用户名已存在"),

    /**
     *  数据库相关异常
     */
    DB_INSERT_ERROR(20001, "DB插入失败"),
    DB_UPDATE_ERROR(20002, "DB更新失败"),
    DB_DELETE_ERROR(20003, "DB删除失败"),

    /**
     *  聊天相关异常
     */
    CHAT_SESSION_NOT_EXIST(30001, "会话不存在"),
    CHAT_ROLE_UNKNOWN(30002, "未知聊天角色"),
    CHAT_HISTORY_NOT_BELONG_TO_CURRENT_USER(30003, "非当前用户会话记录"),

    /**
     *  讲座相关异常
     */
    LECTURE_NOT_EXIST(40001, "讲座不存在"),
    LECTURE_NOT_BELONG_TO_CURRENT_ADMIN(40002, "非当前讲师讲座"),
    LECTURE_STATUS_INVALID(40003, "讲座状态无效"),
    LECTURE_ALREADY_FULL(40004, "讲座已选满"),
    LECTURE_USER_ALREADY_ENROLLED(40005, "用户已报名"),
    LECTURE_USER_NOT_ENROLLED(40006, "用户未报名"),
    LECTURE_STATUS_APPROVED_CANNOT_DELETE(40007, "已通过审核的讲座不能删除"),

    /**
     *  文件上传相关异常
     */
    FILE_TYPE_NOT_SUPPORT(50001, "不支持的文件类型"),
    FILE_UPLOAD_FAILED(50002, "文件上传失败"),

    /**
     *  系统异常：用于在通用系统错误时抛出
     */
    SYSTEM_ERROR(90001, "系统异常"),
    SYSTEM_DIR_CREATE_ERROR(90002, "系统创建目录失败");

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
