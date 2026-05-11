package com.kai.bar_manager.staff.dto;

import com.kai.bar_manager.staff.entity.StaffRole;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "userPassword")
public class AuthRequest {
    private String userName;
    private String userPassword;
    private StaffRole role;
    private Long staffId;
}