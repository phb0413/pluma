package com.park.pluma.controller;

import com.park.pluma.dto.SignUpRequest;
import com.park.pluma.entity.User;
import com.park.pluma.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입 api
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest signUpRequest) {

        User user = userService.signup(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );

        return ResponseEntity.ok(Map.of(
                "message", "회원가입 성공!",
                "username", user.getUsername()
        ));
    }

    // 아이디 중복검사
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {

        boolean exists = userService.existsByUsername(username);

        return ResponseEntity.ok(exists);
    }

    // 이메일 중복검사
    @GetMapping("check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {

        boolean exists = userService.existsByEmail(email);

        return ResponseEntity.ok(exists);
    }
}
