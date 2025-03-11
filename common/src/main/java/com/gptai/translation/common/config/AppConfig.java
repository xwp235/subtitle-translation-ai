package com.gptai.translation.common.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gptai.translation.common.config.serializer.LocalDateTimeSerializer;
import com.gptai.translation.common.config.serializer.ZonedDateTimeSerializer;
import com.gptai.translation.common.constants.AppConstants;
import com.gptai.translation.common.filters.EntryPointFilter;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Configuration
@ConfigurationPropertiesScan
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/404").setViewName("404");
        registry.addViewController("/403").setViewName("403");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 所有api接口都应用此配置
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOriginPatterns(CorsConfiguration.ALL)
                // 允许所有header
                .allowedHeaders(CorsConfiguration.ALL)
                // 允许所有请求方式(GET,POST,...)
                .allowedMethods(CorsConfiguration.ALL)
                .exposedHeaders(AppConstants.X_AUTHENTICATE, AppConstants.X_REQUESTED_ID)
                // 允许请求带上cookie
                .allowCredentials(true)
                // 一小时内不再需要预检（发送OPTIONS请求）
                .maxAge(3600);
    }

    @Bean
    com.fasterxml.jackson.databind.Module simpleModule(ZonedDateTimeSerializer zonedDateTimeSerializer,
                                                       LocalDateTimeSerializer localDateTimeSerializer) {
        var simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Float.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Float.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(ZonedDateTime.class, zonedDateTimeSerializer);
        simpleModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        return simpleModule;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        // 允许特定的域名（可以使用 "*" 允许所有域名）
        configuration.setAllowedOrigins(List.of(CorsConfiguration.ALL));
        // 允许的 HTTP 方法，如 GET、POST 等
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        // 允许的请求头（可以使用 "*" 允许所有头部）
        configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        // 是否允许发送 Cookie
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of(AppConstants.X_AUTHENTICATE, AppConstants.X_REQUESTED_ID));
        // 一小时内不再需要预检（发送OPTIONS请求）
        configuration.setMaxAge(3600L);
        // 配置路径匹配策略
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    FilterRegistrationBean<EntryPointFilter> entryPointFilter() {
        var registrationBean = new FilterRegistrationBean<EntryPointFilter>();
        registrationBean.setFilter(new EntryPointFilter());
        registrationBean.setUrlPatterns(List.of("/*"));
        registrationBean.setEnabled(true);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
