package com.kai.bar_manager.staff.service;

import com.kai.bar_manager.Jwt.JwtUtil;
import com.kai.bar_manager.staff.dto.AuthRequest;
import com.kai.bar_manager.staff.dto.AuthResponse;
import com.kai.bar_manager.staff.dto.StaffRequest;
import com.kai.bar_manager.staff.entity.Staff;
import com.kai.bar_manager.staff.entity.StaffAuth;
import com.kai.bar_manager.staff.entity.StaffRole;
import com.kai.bar_manager.staff.repository.StaffAuthRepository;
import com.kai.bar_manager.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final StaffAuthRepository staffAuthRepository;

    @Transactional
    public Staff createStaff(StaffRequest request) {
        Staff staff = staffRepository.save(
                Staff.builder()
                .name(request.getName())
                .hourlyRate(request.getHourlyRate())
                .accountNumber(request.getAccountNumber())
                .phone(request.getPhone())
                .bank(request.getBank())
                .role(request.getRole())
                .build()
                );
        StaffAuth staffAuth = StaffAuth.builder()
                .userName(request.getName())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .staff(staff)
                .build();

        staffAuthRepository.save(staffAuth);

        return staff;
    }

    @Transactional(readOnly = true)
    public List<Staff> getAllActiveStaff() {
        return staffRepository.findByIsActive(true);
    }

    @Transactional(readOnly = true)
    public List<Staff> getStaffByRole(StaffRole role) {
        return staffRepository.findByRoleAndIsActive(role, true);
    }

    @Transactional
    public Staff updateStaff(Long id, StaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다."));
        staff.setName(request.getName());
        staff.setHourlyRate(request.getHourlyRate());
        staff.setAccountNumber(request.getAccountNumber());
        staff.setPhone(request.getPhone());
        staff.setBank(request.getBank());
        staff.setRole(request.getRole());
        return staffRepository.save(staff);
    }

    @Transactional
    public void deactivateStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다."));
        staff.setIsActive(false);
        staffRepository.save(staff);
    }
}