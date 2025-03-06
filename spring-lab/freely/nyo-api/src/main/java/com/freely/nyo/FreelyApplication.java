package com.freely.nyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	"com.freely.nyo",
	"com.freely.nyodomain",
	"com.freely.nyocore.config",
})
public class FreelyApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreelyApplication.class, args);
	}

}
