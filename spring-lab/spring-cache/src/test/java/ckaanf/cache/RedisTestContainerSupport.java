package ckaanf.cache;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class RedisTestContainerSupport {

	@Container
	static GenericContainer<?> redisContainer = new GenericContainer<>("redis:8.2.1")
		.withExposedPorts(6379);

	@Autowired
	protected StringRedisTemplate redisTemplate;

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
	}

	@BeforeEach
	void cleanData() {
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
	}
}
