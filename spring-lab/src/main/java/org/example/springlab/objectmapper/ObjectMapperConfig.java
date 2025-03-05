package org.example.springlab.objectmapper;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class ObjectMapperConfig {

	// @Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		JavaTimeModule javaTimeModule = new JavaTimeModule();
		LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
			DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);

		objectMapper.registerModule(javaTimeModule);
		return objectMapper;
	}

	// customizer를 등록
	// @Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
				jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class,
					new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyyMMdd")));
				jacksonObjectMapperBuilder.serializerByType(LocalDate.class,
					new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyyMM")));
			}
		};
	}

	// 스프링이 생성한 objectMapper를 받아서 활용
	@Bean
	public ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		jackson2ObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyyMMdd")));
		return jackson2ObjectMapperBuilder.build();
	}
}
