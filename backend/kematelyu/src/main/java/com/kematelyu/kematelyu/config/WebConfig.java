package com.kematelyu.kematelyu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS biar frontend bisa akses API tanpa masalah
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")


            .allowedOrigins("http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*");
    }

    // Serve folder eksternal "static/events" sebagai /events/**
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fullPath = System.getProperty("user.dir") + "/static/events/";
        registry.addResourceHandler("/events/**")
                .addResourceLocations("file:" + fullPath);
    }
}
