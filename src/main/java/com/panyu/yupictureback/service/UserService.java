package com.panyu.yupictureback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.user.UserLoginDTO;
import com.panyu.yupictureback.domain.dto.user.UserRegisterDTO;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yupan
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-12-14 18:50:15
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    ResponseResult<Long> registerUser(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    ResponseResult<UserLoginVO> doLogin(UserLoginDTO userLoginDTO, HttpServletRequest request);
}
