package com.umc.place.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080", "http://exhibition-place.site:8080", "http://43.200.201.162:8080") // 프톤트 배포 후 수정
                .allowedMethods("GET", "POST", "PATCH") // DELETE 허용 X
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Set-Cookie"); // cookie 사용을 위한 HttpServlet 포함 여부
    }
}
