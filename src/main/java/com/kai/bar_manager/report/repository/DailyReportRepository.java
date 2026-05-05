package com.kai.bar_manager.report.repository;

import com.kai.bar_manager.report.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    Optional<DailyReport> findByDate(LocalDate date);
    List<DailyReport> findByDateBetween(LocalDate start, LocalDate end);
    Optional<DailyReport> findTopByOrderByDateDesc();
}