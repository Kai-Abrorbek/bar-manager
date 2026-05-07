package com.kai.bar_manager.staff.repository;

import com.kai.bar_manager.staff.entity.StaffAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffAuthRepository extends JpaRepository<StaffAuth, Long> {
    StaffAuth findByUserName(String userName);
}
