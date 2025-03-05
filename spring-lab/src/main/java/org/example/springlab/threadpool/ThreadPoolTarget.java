package org.example.springlab.threadpool;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ThreadPoolTarget {

	// thread가 바뀔 때 공통 된 값을 활용하고 싶은 경우?
	@Async
	public void async() {
		log.info("async. userId: {}", MDC.get("userId"));
	}
}
