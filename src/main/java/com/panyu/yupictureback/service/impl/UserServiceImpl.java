package com.panyu.yupictureback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.constant.CommonConstant;
import com.panyu.yupictureback.domain.dto.user.UserLoginDTO;
import com.panyu.yupictureback.domain.dto.user.UserRegisterDTO;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.mapper.UserMapper;
import com.panyu.yupictureback.service.UserService;
import com.panyu.yupictureback.utils.EncryptUtil;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import com.panyu.yupictureback.utils.UserContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yupan
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-12-14 18:50:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
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
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        ThrowUtil.throwIf(user != null, ErrorCodeEnum.PARAMS_ERROR, "该用户名已存在！");
        // 校验密码
        ThrowUtil.throwIf(!password.equals(rePassword),
                ErrorCodeEnum.PARAMS_ERROR, "两次输入的密码不正确！");
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
    public ResponseResult<UserLoginVO> doLogin(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        // 判断有没有这个用户
        ThrowUtil.throwIf(user == null, ErrorCodeEnum.PARAMS_ERROR, "用户名或密码错误！");
        // 判断密码是否正确
        ThrowUtil.throwIf(!EncryptUtil.encryptPassword(password).equals(user.getPassword()),
                ErrorCodeEnum.PARAMS_ERROR, "用户名或密码错误！");
        // 脱敏
        UserLoginVO currentUser = BeanUtil.copyProperties(user, UserLoginVO.class);
        // 将用户信息存入用户上下文中
        UserContextUtil.setCurrentUser(currentUser);
        // 存入session
        request.getSession().setAttribute(CommonConstant.LOGIN_USER, currentUser);
        return ResultUtil.success(currentUser);
    }
}




