package org.example.esprac.repository;

import org.example.esprac.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    // Custom query methods can be defined here if needed
//    List<Product> findProductByDescriptionMatches(String keyword);
}
