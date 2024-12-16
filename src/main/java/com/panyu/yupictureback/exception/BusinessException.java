package com.panyu.yupictureback.exception;

import com.panyu.yupictureback.enums.ErrorCodeEnum;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 业务异常
 * @create: 2024-12-08 16:27
 **/
@Getter
public class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -5965838987005582927L;
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
