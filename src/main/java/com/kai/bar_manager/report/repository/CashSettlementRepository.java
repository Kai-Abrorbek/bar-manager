package com.kai.bar_manager.report.repository;

import com.kai.bar_manager.report.entity.CashSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashSettlementRepository extends JpaRepository<CashSettlement, Long> {
    List<CashSettlement> findByDailyReportId(Long dailyReportId);
    void deleteByDailyReportId(Long dailyReportId);
}