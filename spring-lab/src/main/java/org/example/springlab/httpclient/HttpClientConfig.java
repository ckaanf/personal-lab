package org.example.springlab.httpclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

	// SpringBoot가 자동으로 등록해주지 않음
	/**
	 * 기본 RestTemplate 인스턴스를 생성하여 반환합니다.
	 *
	 * 이 메서드는 HTTP 요청 처리를 위한 RestTemplate 객체의 기본 인스턴스를 생성합니다.
	 *
	 * @return 생성된 RestTemplate 인스턴스
	 */
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	// RestTemplateBuilder는 bean으로 등록해함
	/**
	 * Spring 또는 서버 환경에서 trace id를 추적하기 위한 RestTemplate 빈을 생성합니다.
	 *
	 * @param builder RestTemplate 인스턴스 생성을 위한 빌더
	 * @return 생성된 RestTemplate 인스턴스
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// RestTemplate에 대한 대체 Spring 3.2.x over
	// trace id 달라짐
	/**
	 * RestClient의 새 인스턴스를 생성하여 반환합니다.
	 *
	 * <p>이 메서드는 정적 팩토리 메서드 {@code RestClient.create()}를 호출하여 RestClient 객체를 생성합니다.</p>
	 *
	 * @return 새로 생성된 RestClient 인스턴스
	 */
	public RestClient restClient() {
		return RestClient.create();
	}

	/**
	 * RestClient 빌더를 사용하여 구성된 RestClient 인스턴스를 생성하고 반환한다.
	 *
	 * @return 빌더로 구성된 RestClient 인스턴스
	 */
	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		return builder.build();
	}
}
