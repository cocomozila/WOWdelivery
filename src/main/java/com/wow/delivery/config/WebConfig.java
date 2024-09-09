package com.wow.delivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wow.delivery.config.filter.ExceptionHandlerFilter;
import com.wow.delivery.config.filter.LoginFilter;
import com.wow.delivery.config.filter.WarmupTokenFilter;
import com.wow.delivery.warmup.WarmupTokenProvider;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final ObjectMapper objectMapper;
    private final WarmupTokenProvider warmupTokenProvider;

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new LoginFilter());
        filter.setOrder(3);
        filter.addUrlPatterns("/*");
        return filter;
    }

    @Bean
    public FilterRegistrationBean warmupTokenFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new WarmupTokenFilter(warmupTokenProvider));
        filter.setOrder(2);
        filter.addUrlPatterns("/*");
        return filter;
    }

    @Bean
    public FilterRegistrationBean exceptionHandlerFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ExceptionHandlerFilter(objectMapper));
        filter.setOrder(1);
        filter.addUrlPatterns("/*");
        return filter;
    }


}
