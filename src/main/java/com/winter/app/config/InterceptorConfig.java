package com.winter.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.winter.app.interceptors.TestInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{

	private TestInterceptor testInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(testInterceptor)
				.addPathPatterns("/notice/list")
				
				;
		
	}
}
