package org.example.graphqlprac.auth;

import org.example.graphqlprac.domain.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String userId) {
        if (userRepository.existsById(userId)) {
            return jwtUtil.generateToken(userId);
        }
        throw new RuntimeException("User not found");
    }
}
