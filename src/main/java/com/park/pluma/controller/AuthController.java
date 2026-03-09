package com.park.pluma.controller;

import com.park.pluma.dto.LoginRequest;
import com.park.pluma.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest,response);
        return Map.of("accessToken",accessToken);
    }

    @PostMapping("/refresh")
    public Map<String,String> refresh(HttpServletRequest request) {
        String accessToken = authService.refresh(request);
        return Map.of("accessToken",accessToken);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        authService.logout(request);
        return "logout success";
    }
}
