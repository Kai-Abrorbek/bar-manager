package com.kai.bar_manager.staff.dto;

import com.kai.bar_manager.staff.entity.StaffRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequest {
    @NotBlank(message = "유저 이름을 입력하세요.")
    String name;

    @NotBlank(message = "유저 비빌번호를 입력하세요.")
    String password;
}
