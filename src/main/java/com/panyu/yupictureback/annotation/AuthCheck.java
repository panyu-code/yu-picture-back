package com.panyu.yupictureback.annotation;

import com.panyu.yupictureback.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-20 21:26
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    /**
     * 必须有该角色
     *
     * @return
     */
    UserRoleEnum mustRole() default UserRoleEnum.COMMON_USER;
}
