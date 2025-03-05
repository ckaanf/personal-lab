package org.example.springlab;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.example.springlab.objectmapper.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MyRestController {

	@GetMapping("/objectMapper")
	public Item getItem() {

		LocalDateTime testTime = LocalDateTime.now();
		LocalDate testDate = LocalDate.now();

		return new Item("001", "Item", testTime, testDate);
	}
}
