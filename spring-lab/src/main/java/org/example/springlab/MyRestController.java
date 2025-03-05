package org.example.springlab;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.example.springlab.objectmapper.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyRestController {

	private final RestTemplate restTemplate;
	private final RestClient restClient;

	/**
	 * 현재 시각과 날짜를 반영한 Item 객체를 생성하여 반환합니다.
	 *
	 * 이 메서드는 시스템의 현재 LocalDateTime과 LocalDate 값을 사용해 Item 인스턴스를 생성하며,
	 * 항목의 ID는 "001", 이름은 "Item"으로 설정됩니다.
	 *
	 * @return ID "001", 이름 "Item", 현재 시각 및 날짜 정보를 포함한 Item 객체
	 */
	@GetMapping("/objectMapper")
	public Item getItem() {

		LocalDateTime testTime = LocalDateTime.now();
		LocalDate testDate = LocalDate.now();

		return new Item("001", "Item", testTime, testDate);
	}

	/**
	 * "/target" 엔드포인트로 GET 요청 시 호출되며, 로그에 "target" 메시지를 기록한 후 "target" 문자열을 반환합니다.
	 *
	 * @return "target" 문자열
	 */
	@GetMapping("/target")
	public String target() {
		log.info("target");
		return "target";
	}

	/**
	 * "http://localhost:8080/target" 주소로 GET 요청을 보내고, 응답 본문을 반환합니다.
	 * 요청 결과로 받은 응답 본문은 로그에 기록됩니다.
	 *
	 * @return GET 요청의 응답 본문을 문자열로 반환
	 */
	@GetMapping("/restTemplate")
	public String restTemplate() {
		ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/target", String.class);
		log.info("body : {} ", forEntity.getBody());
		return forEntity.getBody();
	}

	/**
	 * restClient를 사용하여 "http://localhost:8080/target" 엔드포인트에 GET 요청을 전송하고,
	 * 응답 본문을 로그에 기록한 후 반환합니다.
	 *
	 * @return GET 요청으로 수신한 응답 본문
	 */
	@GetMapping("/restClient")
	public String restClient() {
		String body = restClient.get().uri("http://localhost:8080/target")
			.retrieve()
			.body(String.class);
		log.info("body : {} ", body);
		return body;
	}
}
