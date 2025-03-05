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

	@GetMapping("/objectMapper")
	public Item getItem() {

		LocalDateTime testTime = LocalDateTime.now();
		LocalDate testDate = LocalDate.now();

		return new Item("001", "Item", testTime, testDate);
	}

	@GetMapping("/target")
	public String target() {
		log.info("target");
		return "target";
	}

	@GetMapping("/restTemplate")
	public String restTemplate() {
		ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/target", String.class);
		log.info("body : {} ", forEntity.getBody());
		return forEntity.getBody();
	}

	@GetMapping("/restClient")
	public String restClient() {
		String body = restClient.get().uri("http://localhost:8080/target")
			.retrieve()
			.body(String.class);
		log.info("body : {} ", body);
		return body;
	}
}
