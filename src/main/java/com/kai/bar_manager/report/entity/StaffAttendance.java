package com.kai.bar_manager.report.entity;

import com.kai.bar_manager.staff.entity.Staff;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staff_attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_report_id")
    private DailyReport dailyReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private Double hoursWorked;
    private String startTime;
    private String endTime;
}