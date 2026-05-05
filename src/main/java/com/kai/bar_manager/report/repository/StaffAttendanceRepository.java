package com.kai.bar_manager.report.repository;

import com.kai.bar_manager.report.entity.StaffAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {
    List<StaffAttendance> findByDailyReportId(Long dailyReportId);
    void deleteByDailyReportId(Long dailyReportId);
}