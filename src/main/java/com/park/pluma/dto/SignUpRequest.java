package com.park.pluma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank(message = "사용자 이름은 필수!")
    private String username;

    @NotBlank(message = "email은 필수!")
    @Email(message = "올바른 email 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수!")
    private String password;
}
