package com.park.pluma.service;

import com.park.pluma.entity.User;
import com.park.pluma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(String username, String email, String password) {

        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 username");
        }

        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 email");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .build();

        return userRepository.save(user);
    }

    // 아이디 중복검사
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // 이메일 중복검사
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
