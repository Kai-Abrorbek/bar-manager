package com.kai.bar_manager.report.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cash_expenditure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_report_id")
    private DailyReport dailyReport;

    private String description;
    private Double amount;
}