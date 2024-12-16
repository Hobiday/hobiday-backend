package com.example.hobiday_backend.global.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Value("${property.url.rootUrl}")
    private String rootUrl;
    @Value("${property.url.subUrl}")
    private String subUrl;
    @Value("${property.url.developUrl}")
    private String developUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedOrigins(rootUrl, subUrl, developUrl, "https://api.hobiday.site", "http://api.hobiday.site")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}