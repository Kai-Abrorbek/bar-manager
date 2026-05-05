package com.kai.bar_manager.report.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cash_settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_report_id")
    private DailyReport dailyReport;

    @Enumerated(EnumType.STRING)
    private Denomination denomination;

    private Integer count;
}