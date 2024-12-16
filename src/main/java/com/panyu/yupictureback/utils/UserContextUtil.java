package com.panyu.yupictureback.utils;

import com.panyu.yupictureback.domain.vo.user.UserLoginVO;

/**
 * @author: YuPan
 * @Desc: 用户上下文工具类
 * @create: 2024-12-14 20:43
 **/
public class UserContextUtil {
    private static final ThreadLocal<UserLoginVO> tl = new ThreadLocal<>();

    private UserContextUtil() {
    }

    /**
     * 设置当前登录用户的值
     *
     * @param userLoginVO
     */
    public static void setCurrentUser(UserLoginVO userLoginVO) {
        tl.set(userLoginVO);
    }

    /**
     * 获取当前登录用户的值
     *
     * @return
     */
    public static UserLoginVO getCurrentUser() {
        return tl.get();
    }

    /**
     * 注销当前登录用户
     */
    public static void removeCurrentUser() {
        tl.remove();
    }
}
