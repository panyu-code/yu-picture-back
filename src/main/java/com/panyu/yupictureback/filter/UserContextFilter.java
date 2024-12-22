package com.panyu.yupictureback.filter;

import com.panyu.yupictureback.constant.CommonConstant;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.utils.UserContextUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-14 21:19
 **/
@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 从 HttpSession 获取当前用户信息
        UserLoginVO user = getUserFromSession(httpRequest);

        // 将用户信息存储到 ThreadLocal
        UserContextUtil.setCurrentUser(user);

        // 继续请求的处理
        chain.doFilter(request, response);

        // 请求处理完后清理 ThreadLocal 中的数据，避免内存泄漏
        UserContextUtil.removeCurrentUser();
    }

    private UserLoginVO getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (UserLoginVO) session.getAttribute(CommonConstant.LOGIN_USER);  // 假设用户信息保存在 session 中
    }
}
