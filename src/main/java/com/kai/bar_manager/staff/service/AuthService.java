package com.kai.bar_manager.staff.service;

import com.kai.bar_manager.Jwt.JwtUtil;
import com.kai.bar_manager.staff.dto.AuthRequest;
import com.kai.bar_manager.staff.dto.AuthResponse;
import com.kai.bar_manager.staff.entity.StaffAuth;
import com.kai.bar_manager.staff.repository.StaffAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StaffAuthRepository staffAuthRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        StaffAuth staffAuth = staffAuthRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(request.getUserPassword(), staffAuth.getUserPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        String token = jwtUtil.generateToken(staffAuth.getUserName());

        return AuthResponse.builder()
                .token(token)
                .name(staffAuth.getStaff().getName())
                .role(staffAuth.getRole().name())
                .build();
    }
}