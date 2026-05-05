package com.kai.bar_manager.report.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_report_id")
    private DailyReport dailyReport;

    @Enumerated(EnumType.STRING)
    private PurchaseCategory category;

    private String itemName;
    private Double amount;
}