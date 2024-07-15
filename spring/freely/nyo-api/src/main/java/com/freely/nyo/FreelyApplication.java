package com.freely.nyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = {
	"com.freely.nyo",
	"com.freely.nyodomain",
	//TODO config로 하면 안되고 nyocore로 해야하는 이유?
	"com.freely.nyocore",
})
public class FreelyApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreelyApplication.class, args);
	}

}
