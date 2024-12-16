package com.panyu.yupictureback.utils;

import org.springframework.util.DigestUtils;


/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-14 19:21
 **/
public class EncryptUtil {
    private static final String PASSWORD_SALT = "yu_pan";

    /**
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex((PASSWORD_SALT + password).getBytes());
    }
}
