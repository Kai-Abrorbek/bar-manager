package com.kai.bar_manager.report.service;

import com.kai.bar_manager.report.entity.*;
import com.kai.bar_manager.report.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    @Value("${excel.template-path}")
    private String templatePath;

    @Value("${excel.output-path}")
    private String outputPath;

    private final DailyReportRepository dailyReportRepository;
    private final CashSettlementRepository cashSettlementRepository;
    private final StaffAttendanceRepository staffAttendanceRepository;
    private final PurchaseRepository purchaseRepository;
    private final CashExpenditureRepository cashExpenditureRepository;

    // 매달 초 템플릿 복사해서 새 파일 생성
    public void createMonthlyFile(int year, int month) throws Exception {
        Path template = Paths.get(templatePath);
        Path outputDir = Paths.get(outputPath);

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        Path output = outputDir.resolve(year + "년_" + month + "월_일마감.xlsx");
        Files.copy(template, output, StandardCopyOption.REPLACE_EXISTING);
    }

    // 특정 날짜 데이터 엑셀에 입력
    public void writeDailyReport(LocalDate date) throws Exception {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        Path filePath = Paths.get(outputPath + year + "년_" + month + "월_일마감.xlsx");

        // 파일 없으면 템플릿에서 생성
        if (!Files.exists(filePath)) {
            createMonthlyFile(year, month);
        }

        DailyReport report = dailyReportRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("해당 날짜 보고서가 없습니다."));

        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook = new XSSFWorkbook(Files.newInputStream(filePath));
        Sheet sheet = workbook.getSheet("일마감보고서");

        // 섹션 시작 행 계산 (1일 = 행1, 2일 = 행39, 간격 38)
        int startRow = (day - 1) * 38;

        // 기본 정보 입력
        setValue(sheet, startRow + 3, 4, report.getReceiptCount());  // E3 영수건수
        setValue(sheet, startRow + 3, 2, report.getSales());          // C4 매출
        setValue(sheet, startRow + 3, 4, report.getTablePrice());     // E4 테블단가
        setValue(sheet, startRow + 13, 2, report.getCashSales());     // C14 현금매출
        setValue(sheet, startRow + 25, 5, report.getDepositAmount()); // F26 입금액

        // 마감정산 개수 입력
        List<CashSettlement> settlements = cashSettlementRepository.findByDailyReportId(report.getId());
        for (CashSettlement s : settlements) {
            int settlementRow = getSettlementRow(s.getDenomination());
            setValue(sheet, startRow + settlementRow, 5, s.getCount()); // F열
        }

        // 직원 출근 입력 (J열 = 9)
        List<StaffAttendance> attendances = staffAttendanceRepository.findByDailyReportId(report.getId());
        int attRow = startRow + 3; // J4부터 시작
        for (StaffAttendance a : attendances) {
            if (a.getStaff().getRole() == com.kai.bar_manager.staff.entity.StaffRole.STAFF) {
                setValue(sheet, attRow, 9, a.getHoursWorked()); // J열
                attRow++;
            }
        }

        // 알바 입력 (I, J, K열)
        int albaRow = startRow + 14; // 15행부터
        for (StaffAttendance a : attendances) {
            if (a.getStaff().getRole() == com.kai.bar_manager.staff.entity.StaffRole.ALBA) {
                setValue(sheet, albaRow, 8, a.getStaff().getName() +
                        (a.getStartTime() != null ? a.getStartTime() + "-" + a.getEndTime() : "")); // I열
                setValue(sheet, albaRow, 9, a.getHoursWorked()); // J열
                setValue(sheet, albaRow, 10, a.getStaff().getHourlyRate()); // K열
                albaRow++;
            }
        }

        // 현금지출 입력
        List<CashExpenditure> expenditures = cashExpenditureRepository.findByDailyReportId(report.getId());
        int expRow = startRow + 22; // 23행부터
        for (CashExpenditure e : expenditures) {
            setValue(sheet, expRow, 2, e.getDescription()); // C열
            setValue(sheet, expRow, 3, e.getAmount());       // D열
            expRow++;
        }

        // 파일 저장
        FileOutputStream fos = new FileOutputStream(filePath.toFile());
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    // 엑셀 파일 다운로드
    public byte[] downloadMonthlyExcel(int year, int month) throws Exception {
        Path filePath = Paths.get(outputPath + year + "년_" + month + "월_일마감.xlsx");

        if (!Files.exists(filePath)) {
            createMonthlyFile(year, month);
        }

        return Files.readAllBytes(filePath);
    }

    private void setValue(Sheet sheet, int rowIdx, int colIdx, Object value) {
        Row row = sheet.getRow(rowIdx);
        if (row == null) row = sheet.createRow(rowIdx);
        Cell cell = row.getCell(colIdx);
        if (cell == null) cell = row.createCell(colIdx);

        if (value instanceof String) cell.setCellValue((String) value);
        else if (value instanceof Double) cell.setCellValue((Double) value);
        else if (value instanceof Integer) cell.setCellValue((Integer) value);
        else if (value instanceof Long) cell.setCellValue((Long) value);
    }

    private int getSettlementRow(Denomination denomination) {
        return switch (denomination) {
            case TEN_THOUSAND_WON -> 14;  // F15
            case FIFTY_THOUSAND_WON -> 15; // F16
            case TEN_THOUSAND -> 16;       // F17
            case FIVE_THOUSAND -> 17;      // F18
            case ONE_THOUSAND -> 18;       // F19
            case FIVE_HUNDRED -> 19;       // F20
            case ONE_HUNDRED -> 20;        // F21
        };
    }
}