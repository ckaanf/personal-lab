package com.freely.nyo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freely.nyo.dto.ProductRequest;
import com.freely.nyo.dto.ProductResponse;
import com.freely.nyodomain.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") long productId) {
		ProductResponse productResponse = ProductResponse.from(productService.getProduct(productId));
		return ResponseEntity.ok(productResponse);
	}

	@PostMapping
	public ResponseEntity<Void> saveProduct(@RequestBody ProductRequest productRequest) {
		productService.saveProduct(ProductRequest.to(productRequest));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// 예외 처리 예시
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body("An error occurred: " + ex.getMessage());
	}
}
