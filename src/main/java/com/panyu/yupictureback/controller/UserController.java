package com.panyu.yupictureback.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panyu.yupictureback.annotation.AuthCheck;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.user.*;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.user.UserListVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: YuPan
 * @Desc: UserController
 * @create: 2024-12-14 18:54
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseResult<Long> registerUser(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        log.info("用户注册入参：{}", userRegisterDTO);
        return userService.registerUser(userRegisterDTO);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseResult<UserLoginVO> doLogin(@RequestBody @Validated UserLoginDTO userLoginDTO,
                                               HttpServletRequest request, HttpServletResponse response) {
        log.info("用户登录入参：{}", userLoginDTO);
        return userService.doLogin(userLoginDTO, request,response);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public ResponseResult<Boolean> doLogout(HttpServletRequest request,HttpServletResponse response) {
        return userService.doLogout(request,response);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @ApiOperation("获取当前登录用户")
    @GetMapping("/current")
    public ResponseResult<UserLoginVO> getCurrentUser() {
        return userService.getCurrentUser();
    }

    /**
     * 新增用户-管理员权限
     *
     * @param userAddDTO
     * @return
     */
    @ApiOperation("新增用户-管理员权限")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Boolean> addUser(@RequestBody @Validated UserAddDTO userAddDTO) {
        log.info("新增用户入参：{}", userAddDTO);
        return userService.addUser(userAddDTO);
    }

    /**
     * 修改用户
     *
     * @param userUpdateDTO
     * @return
     */
    @ApiOperation("修改用户")
    @PostMapping("/update")
    public ResponseResult<Boolean> updateUser(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        log.info("修改用户入参：{}", userUpdateDTO);
        return userService.updateUser(userUpdateDTO);
    }

    /**
     * 删除用户-批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation("删除用户-批量删除")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Boolean> deleteUser(@RequestBody List<Long> ids) {
        log.info("删除用户入参：{}", ids);
        return userService.deleteUser(ids);
    }

    /**
     * 查询用户列表
     *
     * @param userQueryDTO
     * @return
     */
    @ApiOperation("查询用户列表")
    @PostMapping("/list")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Page<UserListVO>> listUser(@RequestBody @Validated UserQueryDTO userQueryDTO) {
        log.info("查询用户列表入参：{}", userQueryDTO);
        return userService.listUser(userQueryDTO);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id获取用户信息")
    @GetMapping("/id/{id}")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<User> getUserById(@PathVariable("id") Long id) {
        log.info("根据id获取用户信息入参：{}", id);
        return userService.getUserById(id);
    }
}
