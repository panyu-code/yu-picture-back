package com.panyu.yupictureback.aop;

import com.panyu.yupictureback.annotation.AuthCheck;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.utils.UserContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-20 21:28
 **/
@Aspect
@Component
public class AuthAspect {
    /**
     * 权限校验
     *
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 获取必须要拥有的角色枚举
        UserRoleEnum mustRole = authCheck.mustRole();
        // 不需要权限，放行
        if (mustRole == null || mustRole.equals(UserRoleEnum.COMMON_USER)) {
            return joinPoint.proceed();
        }
        // 获取当前用户
        UserLoginVO currentUser = UserContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN_ERROR);
        }
        // 获取当前角色拥有的权限
        UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue(currentUser.getRole());
        if (roleEnum == null) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
        }
        // 必须是管理员且当前用户不是管理员
        if (UserRoleEnum.ADMIN.equals(mustRole) && !UserRoleEnum.ADMIN.equals(roleEnum)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
        }
        // 非管理员权限，放行
        return joinPoint.proceed();
    }
}
