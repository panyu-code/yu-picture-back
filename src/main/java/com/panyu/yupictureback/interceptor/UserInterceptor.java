package com.panyu.yupictureback.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panyu.yupictureback.constant.CommonConstant;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2025-01-05 03:13
 **/
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ObjectMapper objectMapper = new ObjectMapper();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CommonConstant.LOGIN_USER.equals(cookie.getName())) {
                    try {
                        String encryptedUser = cookie.getValue();
                        String userJson = new String(Base64.getDecoder().decode(encryptedUser));
                        UserLoginVO userLoginVO = objectMapper.readValue(userJson, UserLoginVO.class);
                        UserContextUtil.setCurrentUser(userLoginVO);
                        return true;
                    } catch (Exception e) {
                        log.error("用户信息解析失败", e);
                        throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, "用户信息解析失败");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextUtil.removeCurrentUser();
    }
}
