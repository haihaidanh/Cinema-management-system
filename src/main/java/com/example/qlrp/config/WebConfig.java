package com.example.qlrp.config;

import com.example.qlrp.interceptor.CustomerAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CustomerAuthInterceptor customerAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Tất cả trang đều yêu cầu đăng nhập (Tiền điều kiện nghiệp vụ)
        // Ngoại trừ: trang login, tài nguyên tĩnh
        registry.addInterceptor(customerAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/css/**", "/js/**", "/images/**", "/error");
    }
}
