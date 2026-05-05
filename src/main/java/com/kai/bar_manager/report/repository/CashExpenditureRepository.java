package com.kai.bar_manager.report.repository;

import com.kai.bar_manager.report.entity.CashExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashExpenditureRepository extends JpaRepository<CashExpenditure, Long> {
    List<CashExpenditure> findByDailyReportId(Long dailyReportId);
    void deleteByDailyReportId(Long dailyReportId);
}