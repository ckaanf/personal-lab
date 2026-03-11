package ckaanf.cache;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest extends RedisTestContainerSupport {

	@Test
	void test1() {
		redisTemplate.opsForValue().set("mykey", "myvalue");
		String result = redisTemplate.opsForValue().get("mykey");
		System.out.println("result = " + result);
	}

	@Test
	void test2() {
		String result = redisTemplate.opsForValue().get("mykey");
		System.out.println("result = " + result);
	}

	@Test
	void test3() {
		String result = redisTemplate.opsForValue().get("mykey");
		System.out.println("result = " + result);
	}
}
