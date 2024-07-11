package com.freely.nyo.repository.user;

import org.springframework.data.crossstore.ChangeSetPersister;

import com.freely.nyo.domain.User;

public interface UserRepository {
	public User findById(long id) throws ChangeSetPersister.NotFoundException;

	public void save(User user);
}
