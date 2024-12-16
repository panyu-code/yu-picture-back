package com.panyu.yupictureback.enums;

import lombok.Getter;

/**
 * @author: YuPan
 * @Desc: 错误码枚举
 * @create: 2024-12-08 16:09
 **/
@Getter
public enum ErrorCodeEnum {
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误！"),
    PARAMS_NOT_VALID(40001, "输入的参数不合法！"),
    NOT_LOGIN_ERROR(40100, "未登录，请先登录！"),
    NO_AUTH_ERROR(40101, "暂无权限，请联系管理员！"),
    NOT_FOUND_ERROR(40400, "请求数据不存在！"),
    FORBIDDEN_ERROR(40300, "禁止访问！"),
    SYSTEM_ERROR(50000, "系统内部异常！"),
    OPERATION_ERROR(50001, "操作失败！");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
