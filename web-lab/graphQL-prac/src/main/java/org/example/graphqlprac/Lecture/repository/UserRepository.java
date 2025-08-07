package org.example.graphqlprac.Lecture.repository;

import org.example.graphqlprac.Lecture.domain.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, String> {
    User save(User user);

    Optional<User> findById(String userId);
}
