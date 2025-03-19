package com.library;

import com.library.feign.KakaoClient;
import com.library.feign.NaverClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {NaverClient.class, KakaoClient.class})
@SpringBootApplication
public class LibrarySearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibrarySearchApplication.class, args);
	}
}