package com.example.demo.config;

import com.example.demo.component.LoginHandlerInterceptor;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter() {
        WebMvcConfigurer adapter = new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry){
                registry.addViewController("/login").setViewName("/");
                registry.addViewController("/registry.html").setViewName("registry");
            }
            @Override
            public void addInterceptors(InterceptorRegistry registry) {

                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/")
                        .excludePathPatterns("/login.html")
                        .excludePathPatterns("/registry")
                        .excludePathPatterns("/forgetPassword")
                        .excludePathPatterns("/resetPassword")
                        .excludePathPatterns("/css/**")
                        .excludePathPatterns("/activation")
                        .excludePathPatterns("/face/**");
            }
        };
        return adapter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        ApplicationHome home = new ApplicationHome();
        home.getDir();
        home.getSource();
        String dir = home.toString().replace("\\", "/") + "/face/";
        registry.addResourceHandler("/face/**").addResourceLocations("file:"+dir);
    }
}