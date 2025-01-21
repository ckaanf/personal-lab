package com.example.redis_practice.leaderboard.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.redis_practice.leaderboard.service.RankingService;

@RestController
public class ApiController {

	private final RankingService rankingService;

	public ApiController(RankingService rankingService) {
		this.rankingService = rankingService;
	}

	@GetMapping("/setScore")
	public Boolean setScore(@RequestParam String userId, @RequestParam int score) {
		return rankingService.setUserScore(userId, score);
	}

	@GetMapping("/getRank")
	public Long getUserRank(@RequestParam String userId) {
		return rankingService.getUserRanking(userId);
	}

	@GetMapping("/getTopRank")
	public List<String> getTopRank() {
		return rankingService.getTopRank(3);
	}
}
