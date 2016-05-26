/**
 * 
 */
package com.smartcity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.smartcity.business.security.filter.LoginFilter;
import com.smartcity.business.security.filter.TokenFilter;

/**
 * @author gperreas
 *
 */
@Configuration
public class WebConfigurer {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurerAdapter() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**")
	            		.allowedOrigins("http://localhost:8888")
			            .allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders(LoginFilter.HEADER_AUTHORIZATION, 
								TokenFilter.HEADER_TOKEN_KEY, "Content-Type", "Accept");
	        }
	    };
	}
}
