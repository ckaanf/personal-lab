package com.freely.nyocore.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackages = "com.freely.nyocore")
@EnableJpaRepositories(basePackages = {"com.freely.nyocore"})
// config 안먹고 있을 가능성
public class JpaConfig {

}
