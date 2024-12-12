package com.github.walterfan.hellometrics;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Predicate;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * @Author: Walter Fan
 * @Date: 6/8/2020, Thu
 **/
@Configuration
public class WebConfig {
    protected boolean enableSwagger = true;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(Boolean.TRUE)
                .select()
                .apis(getApiSelector())
                //.paths(regex("/potato/api/v1/*"))
                //.paths(Predicates.and(regex("/api.*")))
                .paths(PathSelectors.any())
                .build()
                .enable(enableSwagger)
                .apiInfo(apiInfo());
    }

    protected Predicate<RequestHandler> getApiSelector() {
        return RequestHandlerSelectors.basePackage("com.github.walterfan");
    }

    protected ApiInfo apiInfo() {
        return new ApiInfo(
                "REST API",
                "REST description of API.",
                "1.0",
                "Terms of service",
                new Contact("Walter Fan", "http://www.fanyamin.com", "walter.fan@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

    @Lazy
    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public FilterRegistrationBean<MetricsFilter> loggingFilter(){
        FilterRegistrationBean<MetricsFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new MetricsFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
