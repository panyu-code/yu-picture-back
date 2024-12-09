package com.panyu.yupictureback.exception;

import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: YuPan
 * @Desc: 全局异常处理器
 * @create: 2024-12-08 16:26
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseResult<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtil.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtil.fail(ErrorCodeEnum.SYSTEM_ERROR);
    }
}
