package com.kai.bar_manager.staff.repository;

import com.kai.bar_manager.staff.entity.Staff;
import com.kai.bar_manager.staff.entity.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByRoleAndIsActive(StaffRole role, Boolean isActive);
    List<Staff> findByIsActive(Boolean isActive);
}