package com.freely.nyo.core;

import com.freely.nyo.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "user")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 20)
	private String name;
	private int mileage;

	public static UserEntity from(User user) {
		UserEntity entity = new UserEntity();
		entity.id = user.getId();
		entity.name = user.getName();
		entity.mileage = user.getMileage();
		return entity;
	}

	public User toModel() {
		return User.builder()
			.id(id)
			.name(name)
			.mileage(mileage)
			.build();
	}
}
