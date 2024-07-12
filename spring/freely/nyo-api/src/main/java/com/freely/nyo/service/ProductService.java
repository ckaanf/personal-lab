package com.freely.nyo.service;

import org.springframework.stereotype.Service;

import com.freely.nyo.domain.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Product calculatePrice(Long productId)  {
		return null;
	}
}
