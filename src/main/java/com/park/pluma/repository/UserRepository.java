package com.park.pluma.repository;

import com.park.pluma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // Optional로 null 방지, 명확한 예외처리 가능

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
