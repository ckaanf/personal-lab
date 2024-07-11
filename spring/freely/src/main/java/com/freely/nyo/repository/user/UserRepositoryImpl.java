package com.freely.nyo.repository.user;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freely.nyo.core.UserEntity;
import com.freely.nyo.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User findById(long id) throws ChangeSetPersister.NotFoundException {
		return userJpaRepository.findById(id)
			.orElseThrow(ChangeSetPersister.NotFoundException::new)
			.toModel();

	}

	@Override
	@Transactional
	public void save(User user) {
		userJpaRepository.save(UserEntity.from(user));
	}
}