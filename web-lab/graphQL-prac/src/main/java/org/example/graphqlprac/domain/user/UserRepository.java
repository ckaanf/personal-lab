package org.example.graphqlprac.domain.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, String> {
    User save(User user);

    Optional<User> findById(String userId);

    boolean existsById(String userId);
}
