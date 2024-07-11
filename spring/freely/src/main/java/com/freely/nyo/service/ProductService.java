package com.freely.nyo.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import com.freely.nyo.domain.Product;
import com.freely.nyo.repository.product.ProductRepository;
import com.freely.nyo.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final UserService userService;


	public Product calculatePrice(Long productId) throws ChangeSetPersister.NotFoundException {
		return productRepository.findById(productId);
	}
}
