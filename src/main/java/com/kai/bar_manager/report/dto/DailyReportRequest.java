package com.kai.bar_manager.report.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportRequest {

    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate date;

    @NotNull(message = "매출을 입력해주세요.")
    private Double sales;

    @NotNull(message = "테블단가를 입력해주세요.")
    private Double tablePrice;

    @NotNull(message = "영수건수를 입력해주세요.")
    private Integer receiptCount;
    private Integer before5pm;
    private Double cashSales;
    private Double depositAmount;

    // 마감정산
    private List<CashSettlementDto> cashSettlements;

    // 직원 출근
    private List<StaffAttendanceDto> staffAttendances;

    // 매입
    private List<PurchaseDto> purchases;

    // 현금지출
    private List<CashExpenditureDto> cashExpenditures;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashSettlementDto {
        private String denomination;
        private Integer count;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffAttendanceDto {
        private Long staffId;
        private Double hoursWorked;
        private String startTime;
        private String endTime;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseDto {
        private String category;
        private String itemName;
        private Double amount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashExpenditureDto {
        private String description;
        private Double amount;
    }
}