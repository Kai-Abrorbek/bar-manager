package com.kai.bar_manager.report.repository;

import com.kai.bar_manager.report.entity.Purchase;
import com.kai.bar_manager.report.entity.PurchaseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByDailyReportId(Long dailyReportId);
    List<Purchase> findByDailyReportIdAndCategory(Long dailyReportId, PurchaseCategory category);
    void deleteByDailyReportId(Long dailyReportId);
}