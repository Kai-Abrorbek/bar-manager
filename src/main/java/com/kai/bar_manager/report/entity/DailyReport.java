package com.kai.bar_manager.report.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    private Double sales;
    private Double tablePrice;
    private Integer receiptCount;
    private Integer before5pm;
    private Double cashSales;
    private Double depositAmount;

    // 마감정산 합계 (자동계산)
    private Double settlementTotal;

    // 전날시재 (전날 내일시재가 자동으로 들어옴)
    private Double previousDayCash;

    // 오차
    private Double cashDifference;

    // 내일시재 (자동계산)
    private Double nextDayCash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}