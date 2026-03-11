package org.example.esprac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class EsPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsPracApplication.class, args);
    }

}
