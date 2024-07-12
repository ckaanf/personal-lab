package com.freely.nyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@EnableJpaRepositories(basePackages = {"com.freely.nyocore.repository"})
@SpringBootApplication
public class FreelyApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreelyApplication.class, args);
	}

}
