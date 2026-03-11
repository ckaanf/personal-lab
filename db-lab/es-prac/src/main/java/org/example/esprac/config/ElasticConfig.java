package org.example.esprac.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 좀 더 일반적인 방법
 * 상세한 control 가능
 */
@EnableElasticsearchRepositories
@Configuration
public class ElasticConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.uris}")
    private String host;


    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host)
                .build();
    }
}
