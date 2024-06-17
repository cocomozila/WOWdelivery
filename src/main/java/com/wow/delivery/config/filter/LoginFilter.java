package com.wow.delivery.config.filter;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.AuthenticationException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String COOKIE_NAME = "DELIVERY";
    private static final String[] whitelist = {"/api/users/signin", "/api/users/signup", "/api/owners/signin", "/api/owners/signup"};

    private StringRedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext context = filterConfig.getServletContext();
        redisTemplate = WebApplicationContextUtils.getRequiredWebApplicationContext(context)
            .getBean(StringRedisTemplate.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        if (isLoginCheckPath(requestURI)) {
            String cookieValue = getCookieValue(request);
            HttpSession session = request.getSession(false);

            if (cookieValue == null || session == null) {
                throw new AuthenticationException(ErrorCode.UNAUTHENTICATED, "로그인이 필요합니다.");
            }

            String sessionKey = "spring:session:sessions:" + cookieValue;
            if (!redisTemplate.hasKey(sessionKey)) {
                throw new AuthenticationException(ErrorCode.UNAUTHENTICATED, "로그인이 필요합니다.");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    private String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
