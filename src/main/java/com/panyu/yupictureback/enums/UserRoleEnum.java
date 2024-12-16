package com.panyu.yupictureback.enums;

import cn.hutool.core.util.ObjectUtil;
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

    /**
     * 是否是管理员
     *
     * @param userRole
     * @return
     */
    public static boolean isAdmin(Integer userRole) {
        return ADMIN.getValue().equals(userRole);
    }

    /**
     * 根据对应值获取枚举
     *
     * @param value
     * @return
     */
    public static UserRoleEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
