package org.example.esprac.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "product_info")
public class Product {
    @Id
    String productId;
    String productName;
    double price;
    String description;
    // categoryId
}
