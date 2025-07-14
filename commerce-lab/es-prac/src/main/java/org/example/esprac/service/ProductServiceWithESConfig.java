package org.example.esprac.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.example.esprac.entity.Product;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProductServiceWithESConfig {
    private final ElasticsearchClient esClient;

    public ProductServiceWithESConfig(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public String registerProduct(Product product) {
        try {
            IndexRequest<Product> request = IndexRequest.of(i -> i
                    .index("product_info")
                    .id(product.getProductId())
                    .document(product));

            IndexResponse response = esClient.index(request);
            return response.id(); // Returns the ID of the indexed document
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to index product", e);
        }
    }
}
