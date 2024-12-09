package com.panyu.yupictureback.common;

import com.panyu.yupictureback.enums.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 统一返回对象
 * @create: 2024-12-08 16:06
 **/
@Data
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 7627858500361692927L;
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    public ResponseResult(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public ResponseResult(int code, T data) {
        this(code, data, "ok");
    }

    public ResponseResult(int code, String msg) {
        this(code, null, msg);
    }

    public ResponseResult(ErrorCodeEnum errorCodeEnum) {
        this(errorCodeEnum.getCode(), null, errorCodeEnum.getMessage());
    }
}
