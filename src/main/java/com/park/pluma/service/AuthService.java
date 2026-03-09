package com.park.pluma.service;

import com.park.pluma.config.JwtProvider;
import com.park.pluma.dto.LoginRequest;
import com.park.pluma.entity.User;
import com.park.pluma.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public String login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("해당 user를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("password가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(loginRequest.getUsername());
        String refreshToken = jwtProvider.createRefreshToken(loginRequest.getUsername());

        redisService.saveRefreshToken(user.getUsername(), refreshToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);

        response.addCookie(cookie);

        return accessToken;
    }

    public String refresh(HttpServletRequest request) {
        String refreshToken = null;

        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if(refreshToken == null) {
            throw new RuntimeException("refreshToken 없음");
        }

        String username = jwtProvider.getUsername(refreshToken);

        String savedToken = redisService.getRefreshToken(username);

        if(!refreshToken.equals(savedToken)) {
            throw new RuntimeException("refreshToken 불일치");
        }

        return jwtProvider.createAccessToken(username);
    }

    public void logout(HttpServletRequest request) {
        String refreshToken = null;

        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if(refreshToken != null) {
            String username = jwtProvider.getUsername(refreshToken);
            redisService.deleteRefreshToken(username);
        }
    }
}
