package org.example.springlab.objectmapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Item {
	private final String id;
	private final String name;
	private final LocalDateTime localDateTime;
	private final LocalDate localDate;

}

