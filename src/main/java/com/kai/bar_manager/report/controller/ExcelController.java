package com.kai.bar_manager.report.controller;

import com.kai.bar_manager.report.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(
            @RequestParam int year,
            @RequestParam int month) throws Exception {

        byte[] excelData = excelService.downloadMonthlyExcel(year, month);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                year + "년_" + month + "월_일마감.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @PostMapping("/create-monthly")
    public ResponseEntity<String> createMonthlyFile(
            @RequestParam int year,
            @RequestParam int month) throws Exception {
        excelService.createMonthlyFile(year, month);
        return ResponseEntity.ok("파일 생성 완료");
    }

    @PostMapping("/write/{date}")
    public ResponseEntity<String> writeDailyReport(@PathVariable LocalDate date) throws Exception {
        excelService.writeDailyReport(date);
        return ResponseEntity.ok("데이터 입력 완료");
    }

}