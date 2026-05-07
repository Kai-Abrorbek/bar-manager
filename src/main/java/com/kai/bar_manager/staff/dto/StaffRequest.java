package com.kai.bar_manager.staff.dto;

import com.kai.bar_manager.staff.entity.StaffRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StaffRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "직원 유형을 선택해주세요.")
    private StaffRole role;

    private Double hourlyRate;
    private String password;
    private String accountNumber;
    private String phone;
    private String bank;
}