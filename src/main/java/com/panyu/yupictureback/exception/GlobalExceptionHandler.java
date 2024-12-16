package com.panyu.yupictureback.exception;

import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * @author: YuPan
 * @Desc: 全局异常处理器
 * @create: 2024-12-08 16:26
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<Object> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtil.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<Object> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtil.fail(ErrorCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        Optional.of(bindingResult.getFieldErrors()).ifPresent(errors -> errors.forEach(error ->
                sb.append(error.getField())
                        .append(error.getDefaultMessage())
                        .append(", 当前值: '")
                        .append(error.getRejectedValue())
                        .append("'; ")

        ));
        return ResultUtil.fail(ErrorCodeEnum.PARAMS_NOT_VALID.getCode(), sb.toString());
    }
}
