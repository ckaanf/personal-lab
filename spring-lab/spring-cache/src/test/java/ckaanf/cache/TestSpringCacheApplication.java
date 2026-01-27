package ckaanf.cache;

import org.springframework.boot.SpringApplication;

public class TestSpringCacheApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringCacheApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
