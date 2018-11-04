package com.github.walterfan.blog;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

//-Dspring.profiles.active=ENV_NAME"

@Configuration
public class WebConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    @Profile("prod")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

}
