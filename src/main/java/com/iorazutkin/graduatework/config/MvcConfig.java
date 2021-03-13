package com.iorazutkin.graduatework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
  @Value("${upload.path}")
  private String uploadPath;

  @Override
  public void addViewControllers (ViewControllerRegistry registry) {
    registry.addViewController("/student").setViewName("student");
    registry.addViewController("/teacher").setViewName("teacher");
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/api/**").setViewName("api");
  }

  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/theory/**")
      .addResourceLocations("file://" + uploadPath + "/theory/");
    registry.addResourceHandler("/practice/**")
      .addResourceLocations("file://" + uploadPath + "/practice/");
    registry.addResourceHandler("/template/**")
      .addResourceLocations("file://" + uploadPath + "/template/");
  }
}
