package com.kai.bar_manager.staff.controller;

import com.kai.bar_manager.common.AdminOnly;
import com.kai.bar_manager.staff.dto.AuthRequest;
import com.kai.bar_manager.staff.dto.StaffRequest;
import com.kai.bar_manager.staff.entity.Staff;
import com.kai.bar_manager.staff.entity.StaffAuth;
import com.kai.bar_manager.staff.entity.StaffRole;
import com.kai.bar_manager.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping("/auth/login")
    public ResponseEntity<String> staffLogin(@RequestBody @Valid AuthRequest authRequest){
        return ResponseEntity.ok(staffService.staffLogin(authRequest));
    }

    @AdminOnly
    @PostMapping
    public ResponseEntity<Staff> createStaff(@RequestBody @Valid StaffRequest request) {
        return ResponseEntity.ok(staffService.createStaff(request));
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllActiveStaff() {
        return ResponseEntity.ok(staffService.getAllActiveStaff());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Staff>> getStaffByRole(@PathVariable StaffRole role) {
        return ResponseEntity.ok(staffService.getStaffByRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody @Valid StaffRequest request) {
        return ResponseEntity.ok(staffService.updateStaff(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateStaff(@PathVariable Long id) {
        staffService.deactivateStaff(id);
        return ResponseEntity.ok("비활성화 완료");
    }
}