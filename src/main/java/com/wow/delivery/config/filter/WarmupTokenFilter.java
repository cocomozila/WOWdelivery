package com.wow.delivery.config.filter;

import com.wow.delivery.warmup.WarmupTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class WarmupTokenFilter implements Filter {

    private final WarmupTokenProvider warmupTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = resolveToken(request);

        // 워밍업 토큰이면 로그인 인증을 우회
        if (StringUtils.hasText(token) && warmupTokenProvider.validateToken(token)) {
            return;
        }

        // 로그인 검증이 필요할 경우 여기서 처리
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
