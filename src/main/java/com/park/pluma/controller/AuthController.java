package com.park.pluma.controller;

import com.park.pluma.config.JwtUtil;
import com.park.pluma.dto.LoginRequest;
import com.park.pluma.dto.SignUpRequest;
import com.park.pluma.entity.User;
import com.park.pluma.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 api

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest signUpRequest) {

        if(userService.isUsernameExist(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }

        if(userService.isEmailExist(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        User user = userService.signup(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );

        return ResponseEntity.ok("회원가입 성공!" + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if(user == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 유저입니다.");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUsername());
        return ResponseEntity.ok(token);
    }
}
