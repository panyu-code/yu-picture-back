package com.panyu.yupictureback.controller;

import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.constant.CommonConstant;
import com.panyu.yupictureback.domain.dto.user.UserLoginDTO;
import com.panyu.yupictureback.domain.dto.user.UserRegisterDTO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.service.UserService;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import com.panyu.yupictureback.utils.UserContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: YuPan
 * @Desc: 用户请求类
 * @create: 2024-12-14 18:54
 **/
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户请求类")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户名在2到10位之间
     *                        密码在8位和20位之间
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseResult<Long> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.registerUser(userRegisterDTO);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseResult<UserLoginVO> doLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        return userService.doLogin(userLoginDTO, request);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public ResponseResult<Void> doLogout(HttpServletRequest request) {
        UserLoginVO currentUser = UserContextUtil.getCurrentUser();
        ThrowUtil.throwIf(currentUser == null, ErrorCodeEnum.NOT_LOGIN_ERROR);
        UserContextUtil.removeCurrentUser();
        request.getSession().removeAttribute(CommonConstant.LOGIN_USER);
        return ResultUtil.success();
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @ApiOperation("获取当前登录用户")
    @GetMapping("/current")
    public ResponseResult<UserLoginVO> getCurrentUser() {
        UserLoginVO currentUser = UserContextUtil.getCurrentUser();
        ThrowUtil.throwIf(currentUser == null, ErrorCodeEnum.NOT_LOGIN_ERROR);
        return ResultUtil.success();
    }
}
