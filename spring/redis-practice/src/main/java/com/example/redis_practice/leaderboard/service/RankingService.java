package com.example.redis_practice.leaderboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

	private static final String LEADERBOARD_KEY = "leaderboard";
	private final StringRedisTemplate redisTemplate;

	public RankingService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public boolean setUserScore(String userId, int score) {
		// sorted set
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();
		zSetOperations.add(LEADERBOARD_KEY, userId, score);

		return true;
	}

	public Long getUserRanking(String userId) {
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();

		return zSetOperations.reverseRank(LEADERBOARD_KEY, userId);
	}

	public List<String> getTopRank(int limit) {
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();
		Set<String> rangeSet = zSetOperations.reverseRange(LEADERBOARD_KEY, 0, limit - 1);

		assert rangeSet != null;
		return new ArrayList<>(rangeSet);
	}

}
