package com.panyu.yupictureback.controller;

import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: YuPan
 * @Desc: 测试接口
 * @create: 2024-12-08 16:32
 **/
@RestController
@Slf4j
@RequestMapping("/test")
@Api(tags = "测试接口")
public class TestController {

    @ApiOperation("测试Hello")
    @GetMapping("/hello")
    public ResponseResult<String> hello() {
        return ResultUtil.success("hello");
    }

    @ApiOperation("测试异常")
    @GetMapping("/error")
    public ResponseResult<String> error() {
        throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
    }
}
