package com.example.redis_practice.session_prac.config;

import java.time.Duration;
import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(10))
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
			);

		HashMap<String, RedisCacheConfiguration> configurationHashMap = new HashMap<>();
		configurationHashMap.put("userAgeCache",
			RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(5)));

		return RedisCacheManager
			.RedisCacheManagerBuilder
			.fromConnectionFactory(connectionFactory)
			.cacheDefaults(configuration)
			.withInitialCacheConfigurations(configurationHashMap)
			.build();
	}
}
