package com.park.pluma.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService; // 자동으로 UserDetailsServiceImpl 주입됨


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        //Authrization 헤더가 있고 "Bearer"로 시작하면
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // bearer 이후로 추출

            // token 검증
            if(jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);

                // UserDetails 불러오기 (여기서 권한 포함)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //spring security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // SecurityContext에 인증 정보 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            logger.debug("Invalid JWT token");
        }
        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
