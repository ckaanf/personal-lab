package org.example.springlab.httpclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

	// SpringBoot가 자동으로 등록해주지 않음
	// @Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	// RestTemplateBuilder는 bean으로 등록해함
	// 별도의 Spring 혹은 서버에서 trace id를 추적하기 위해 사용
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// RestTemplate에 대한 대체 Spring 3.2.x over
	// trace id 달라짐
	// @Bean
	public RestClient restClient() {
		return RestClient.create();
	}

	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		return builder.build();
	}
}
