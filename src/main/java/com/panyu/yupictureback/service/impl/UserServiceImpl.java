package com.panyu.yupictureback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.constant.CommonConstant;
import com.panyu.yupictureback.domain.dto.user.*;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.user.UserListVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.mapper.UserMapper;
import com.panyu.yupictureback.service.UserService;
import com.panyu.yupictureback.utils.EncryptUtil;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import com.panyu.yupictureback.utils.UserContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yupan
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-12-14 18:50:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @Override
    public ResponseResult<Long> registerUser(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String rePassword = userRegisterDTO.getRePassword();
        //检验用户名是否重复（根据用户名去数据库查询，若能查到，则说明重复）
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        ThrowUtil.throwIf(user != null, ErrorCodeEnum.PARAMS_ERROR, "该用户名已存在！");
        // 校验密码
        ThrowUtil.throwIf(!password.equals(rePassword), ErrorCodeEnum.PARAMS_ERROR, "两次输入的密码不正确！");
        // 加密密码
        String encryptPassword = EncryptUtil.encryptPassword(password);
        // 插入
        User registerUser = new User().setUsername(username).setPassword(encryptPassword);
        int row = userMapper.insert(registerUser);
        ThrowUtil.throwIf(row < 1, ErrorCodeEnum.OPERATION_ERROR, "新增用户插入失败！");
        // 返回注册用户id
        return ResultUtil.success(registerUser.getId());
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @Override
    public ResponseResult<UserLoginVO> doLogin(UserLoginDTO userLoginDTO, HttpServletRequest request, HttpServletResponse response) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 判断有没有这个用户，密码是否正确
        ThrowUtil.throwIf(user == null || !EncryptUtil.encryptPassword(password).equals(user.getPassword()), ErrorCodeEnum.PARAMS_ERROR, "用户名或密码错误！");
        // 脱敏
        UserLoginVO currentUser = BeanUtil.copyProperties(user, UserLoginVO.class);
        // 3. 将用户信息加密后存储到 Cookie
        try {
            String userJson = new ObjectMapper().writeValueAsString(currentUser);
            String encryptedUser = Base64.getEncoder().encodeToString(userJson.getBytes());
            Cookie cookie = new Cookie(CommonConstant.LOGIN_USER, encryptedUser);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1天
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (Exception e) {
            log.error("用户信息序列化失败", e);
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, "用户信息序列化失败");
        }
        return ResultUtil.success(currentUser);
    }

    /**
     * 新增用户
     *
     * @param userAddDTO
     * @return
     */
    @Override
    public ResponseResult<Boolean> addUser(UserAddDTO userAddDTO) {
        String username = userAddDTO.getUsername();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        ThrowUtil.throwIf(user != null, ErrorCodeEnum.PARAMS_ERROR, "该用户名已存在！");
        userMapper.insert(new User().setUsername(username).setPassword(EncryptUtil.encryptPassword(CommonConstant.DEFAULT_PASSWORD)));
        return ResultUtil.success(true);
    }

    /**
     * 更新用户
     *
     * @param userUpdateDTO
     * @return
     */
    @Override
    public ResponseResult<Boolean> updateUser(UserUpdateDTO userUpdateDTO) {
        String username = userUpdateDTO.getUsername();
        // 根据id查询用户
        User user = Optional.ofNullable(userMapper.selectById(userUpdateDTO.getId())).orElseThrow(() -> new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户不存在！"));
        // 根据用户名查询用户
        User userByName = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        ThrowUtil.throwIf(userByName != null && !userByName.getId().equals(user.getId()), ErrorCodeEnum.PARAMS_ERROR, "该用户名已存在！");
        userMapper.update(BeanUtil.copyProperties(userUpdateDTO, User.class), new LambdaQueryWrapper<User>().eq(User::getId,
                user.getId()));
        return ResultUtil.success(true);
    }

    @Override
    @Transactional
    public ResponseResult<Boolean> deleteUser(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        List<User> users = userMapper.selectByIds(ids);
        if (CollUtil.isEmpty(users)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "要删除的用户不存在！");
        }
        userMapper.deleteByIds(ids);
        return ResultUtil.success(true);
    }

    /**
     * 查询用户列表
     *
     * @param userQueryDTO
     * @return
     */
    @Override
    public ResponseResult<Page<UserListVO>> listUser(UserQueryDTO userQueryDTO) {
        long current = userQueryDTO.getCurrent();
        long pageSize = userQueryDTO.getPageSize();
        String createTimeFrom = "";
        String createTimeTo = "";
        if (userQueryDTO.getCreateTimeRange() != null && userQueryDTO.getCreateTimeRange().length == 2) {
            createTimeFrom = userQueryDTO.getCreateTimeRange()[0];
            createTimeTo = userQueryDTO.getCreateTimeRange()[1];
        }
        Page<User> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .likeRight(CharSequenceUtil.isNotBlank(userQueryDTO.getUsername()), User::getUsername,
                        userQueryDTO.getUsername())
                .like(CharSequenceUtil.isNotBlank(userQueryDTO.getNickname()), User::getNickname, userQueryDTO.getNickname())
                .ge(ObjectUtil.isNotEmpty(createTimeFrom), User::getCreateTime, createTimeFrom)
                .le(ObjectUtil.isNotEmpty(createTimeTo), User::getCreateTime, createTimeTo)
                .orderByAsc(User::getRole)
                .orderByDesc(User::getCreateTime);
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        long total = userPage.getTotal();
        Page<UserListVO> pageVO = new Page<>(current, pageSize, total);
        pageVO.setRecords(BeanUtil.copyToList(userPage.getRecords(), UserListVO.class));
        return ResultUtil.success(pageVO);
    }

    @Override
    public ResponseResult<User> getUserById(Long id) {
        if (ObjectUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        User user = userMapper.selectById(id);
        if (ObjectUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户不存在！");
        }
        return ResultUtil.success(user);
    }

    @Override
    public ResponseResult<UserLoginVO> getCurrentUser() {
        return ResultUtil.success(UserContextUtil.getCurrentUser());
    }

    @Override
    public ResponseResult<Boolean> doLogout(HttpServletRequest request, HttpServletResponse response) {
        UserLoginVO currentUser = UserContextUtil.getCurrentUser();
        ThrowUtil.throwIf(currentUser == null, ErrorCodeEnum.NOT_LOGIN_ERROR);
        Cookie cookie = new Cookie(CommonConstant.LOGIN_USER, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResultUtil.success(true);
    }

    @Override
    public boolean isAdmin(UserLoginVO user) {
        return ObjectUtil.isNotNull(user) && ObjectUtil.isNotNull(user.getRole()) && Objects.equals(user.getRole(), UserRoleEnum.ADMIN.getValue());
    }

}




