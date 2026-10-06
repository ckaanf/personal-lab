package ckaanf.timeoutlab;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LabController {

    private static final Logger log = LoggerFactory.getLogger(LabController.class);

    private final JdbcTemplate jdbcTemplate;
    private final HikariDataSource dataSource;

    public LabController(JdbcTemplate jdbcTemplate, HikariDataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @GetMapping("/slow")
    public String slow(@RequestParam(defaultValue = "10") int sec) {
        long start = System.currentTimeMillis();
        log.info("[slow] start sec={}", sec);
        jdbcTemplate.queryForObject("SELECT pg_sleep(?)::text", String.class, sec);
        log.info("[slow] end elapsed={}ms", System.currentTimeMillis() - start);
        return "slept " + sec + "s";
    }

    @GetMapping("/heavy")
    public Long heavy(@RequestParam(defaultValue = "30000000") long n) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM generate_series(1, ?)", Long.class, n);
    }

    @GetMapping("/fast")
    public Integer fast() {
        return jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    }

    @GetMapping("/pool")
    public Map<String, Integer> pool() {
        HikariPoolMXBean pool = dataSource.getHikariPoolMXBean();
        return Map.of(
                "active", pool.getActiveConnections(),
                "idle", pool.getIdleConnections(),
                "pending", pool.getThreadsAwaitingConnection(),
                "total", pool.getTotalConnections());
    }
}
