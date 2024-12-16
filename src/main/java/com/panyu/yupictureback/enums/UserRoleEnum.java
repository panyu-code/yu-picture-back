package com.panyu.yupictureback.enums;

import lombok.Getter;

/**
 * @author: YuPan
 * @Desc: 用户角色枚举
 * @create: 2024-12-14 19:38
 **/
@Getter
public enum UserRoleEnum {
    /**
     * 管理员
     */
    ADMIN("admin", 1),
    /**
     * 普通用户
     */
    COMMON_USER("common_user", 2);

    private final String key;
    private final Integer value;

    UserRoleEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }
}
