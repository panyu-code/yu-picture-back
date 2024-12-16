package com.panyu.yupictureback.filter;

import com.panyu.yupictureback.constant.CommonConstant;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.utils.UserContextUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-14 21:19
 **/
// @Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        if (session != null) {
            UserLoginVO currentUser = (UserLoginVO) session.getAttribute(CommonConstant.LOGIN_USER);
            if (currentUser != null) {
                UserContextUtil.setCurrentUser(currentUser);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
