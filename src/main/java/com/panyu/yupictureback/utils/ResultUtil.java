package com.panyu.yupictureback.utils;

import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.enums.ErrorCodeEnum;

/**
 * 返回工具类
 */
public class ResultUtil {
    private ResultUtil() {
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static <T> ResponseResult<T> fail(ErrorCodeEnum errorCode) {
        return new ResponseResult<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static <T> ResponseResult<T> fail(int code, String message) {
        return new ResponseResult<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static <T> ResponseResult<T> fail(ErrorCodeEnum errorCode, String message) {
        return new ResponseResult<>(errorCode.getCode(), null, message);
    }
}
