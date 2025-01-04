package com.panyu.yupictureback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.user.*;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.user.UserListVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    ResponseResult<UserLoginVO> doLogin(UserLoginDTO userLoginDTO, HttpServletRequest request, HttpServletResponse response);

    /**
     * 新增用户
     *
     * @param userAddDTO
     * @return
     */
    ResponseResult<Boolean> addUser(UserAddDTO userAddDTO);

    /**
     * 修改用户
     *
     * @param userUpdateDTO
     * @return
     */
    ResponseResult<Boolean> updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    ResponseResult<Boolean> deleteUser(List<Long> ids);

    /**
     * 查询用户列表
     *
     * @param userQueryDTO
     * @return
     */
    ResponseResult<Page<UserListVO>> listUser(UserQueryDTO userQueryDTO);

    /**
     * 根据id获取用户完整信息
     *
     * @param id
     * @return
     */
    ResponseResult<User> getUserById(Long id);

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    ResponseResult<UserLoginVO> getCurrentUser();

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    ResponseResult<Boolean> doLogout(HttpServletRequest request,HttpServletResponse response);

    /**
     * 判断是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(UserLoginVO user);


}
