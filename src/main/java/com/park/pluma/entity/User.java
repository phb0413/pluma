package com.park.pluma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // 사용자 고유 Id (pk)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름(로그인용 Id, 중복 불가)
    @Column(nullable = false, unique = true)
    private String username;

    // 사용자 메일(중복 불가)
    @Column(nullable = false, unique = true)
    private String email;

    // 사용자 비밀번호(암호화 예정)
    @Column(nullable = false)
    private String password;

    // 생성일
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // DB 저장 전 생성일 자동 시간 설정
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
