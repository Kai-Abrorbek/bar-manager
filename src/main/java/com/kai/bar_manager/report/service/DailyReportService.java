package com.kai.bar_manager.report.service;

import com.kai.bar_manager.report.dto.DailyReportRequest;
import com.kai.bar_manager.report.entity.*;
import com.kai.bar_manager.report.repository.*;
import com.kai.bar_manager.staff.entity.Staff;
import com.kai.bar_manager.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final CashSettlementRepository cashSettlementRepository;
    private final StaffAttendanceRepository staffAttendanceRepository;
    private final PurchaseRepository purchaseRepository;
    private final CashExpenditureRepository cashExpenditureRepository;
    private final StaffRepository staffRepository;

    @Transactional
    public DailyReport createOrUpdateReport(DailyReportRequest request) {
        // 오늘 날짜 보고서 있으면 가져오고 없으면 새로 만들기
        DailyReport report = dailyReportRepository.findByDate(request.getDate())
                .orElse(DailyReport.builder().date(request.getDate()).build());

        // 전날 시재 자동 설정
        if (report.getId() == null) {
            dailyReportRepository.findTopByOrderByDateDesc().ifPresent(prev ->
                    report.setPreviousDayCash(prev.getNextDayCash())
            );
        }

        report.setSales(request.getSales());
        report.setTablePrice(request.getTablePrice());
        report.setReceiptCount(request.getReceiptCount());
        report.setBefore5pm(request.getBefore5pm());
        report.setCashSales(request.getCashSales());
        report.setDepositAmount(request.getDepositAmount());

        DailyReport saved = dailyReportRepository.save(report);

        // 기존 데이터 삭제 후 재저장
        cashSettlementRepository.deleteByDailyReportId(saved.getId());
        staffAttendanceRepository.deleteByDailyReportId(saved.getId());
        purchaseRepository.deleteByDailyReportId(saved.getId());
        cashExpenditureRepository.deleteByDailyReportId(saved.getId());

        // 마감정산
        if (request.getCashSettlements() != null) {
            request.getCashSettlements().forEach(dto -> {
                cashSettlementRepository.save(CashSettlement.builder()
                        .dailyReport(saved)
                        .denomination(Denomination.valueOf(dto.getDenomination()))
                        .count(dto.getCount())
                        .build());
            });
        }

        // 직원 출근
        if (request.getStaffAttendances() != null) {
            request.getStaffAttendances().forEach(dto -> {
                Staff staff = staffRepository.findById(dto.getStaffId())
                        .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다."));
                staffAttendanceRepository.save(StaffAttendance.builder()
                        .dailyReport(saved)
                        .staff(staff)
                        .hoursWorked(dto.getHoursWorked())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .build());
            });
        }

        // 매입
        if (request.getPurchases() != null) {
            request.getPurchases().forEach(dto -> {
                purchaseRepository.save(Purchase.builder()
                        .dailyReport(saved)
                        .category(PurchaseCategory.valueOf(dto.getCategory()))
                        .itemName(dto.getItemName())
                        .amount(dto.getAmount())
                        .build());
            });
        }

        // 현금지출
        if (request.getCashExpenditures() != null) {
            request.getCashExpenditures().forEach(dto -> {
                cashExpenditureRepository.save(CashExpenditure.builder()
                        .dailyReport(saved)
                        .description(dto.getDescription())
                        .amount(dto.getAmount())
                        .build());
            });
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public DailyReport getReportByDate(LocalDate date) {
        return dailyReportRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("해당 날짜의 보고서가 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<DailyReport> getReportsByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return dailyReportRepository.findByDateBetween(start, end);
    }
}