package com.freely.nyocore.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.freely.nyocore.core")
@EnableJpaRepositories(basePackages = "com.freely.nyocore.repository")
public class JpaConfig {

}
