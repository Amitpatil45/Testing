package com.root32.userservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.root32.configsvc")
@EnableJpaRepositories("com.root32")
@EntityScan(basePackages = "com.root32")
public class ExternalBeanConfig {

}
