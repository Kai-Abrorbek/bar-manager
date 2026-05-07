package com.kai.bar_manager.report.controller;

import com.kai.bar_manager.report.dto.DailyReportRequest;
import com.kai.bar_manager.report.entity.DailyReport;
import com.kai.bar_manager.report.service.DailyReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class DailyReportController {

    private final DailyReportService dailyReportService;

    @PostMapping
    public ResponseEntity<DailyReport> createOrUpdateReport(@RequestBody @Valid DailyReportRequest request) {
        return ResponseEntity.ok(dailyReportService.createOrUpdateReport(request));
    }

    @GetMapping("/{date}")
    public ResponseEntity<DailyReport> getReportByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(dailyReportService.getReportByDate(date));
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<DailyReport>> getMonthlyReports(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(dailyReportService.getReportsByMonth(year, month));
    }
}