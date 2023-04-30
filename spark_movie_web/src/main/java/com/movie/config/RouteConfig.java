package com.movie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RouteConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("main.html").setViewName("index");
        registry.addViewController("login.html").setViewName("login");
        registry.addViewController("register.html").setViewName("register");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginFilter()).addPathPatterns("/**")
                .excludePathPatterns("/index", "/user/login", "/login", "/login.html", "/user/register", "/register", "/register.html", "/css/**", "/layui/**", "/images/**", "/js/**");
    }
}
