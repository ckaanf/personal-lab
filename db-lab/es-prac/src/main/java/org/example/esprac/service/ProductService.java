package org.example.esprac.service;

import lombok.RequiredArgsConstructor;
import org.example.esprac.entity.Product;
import org.example.esprac.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    ProductRepository productRepository;

    public List<Product> getProductInfo(String keyword) {
//        return productRepository.findProductByDescriptionMatches(keyword);
        return List.of();
    }

    public Product setProductInfo(Product product) {
        productRepository.save(product);
        return productRepository.findById(product.getProductId()).orElseThrow();
    }
}
