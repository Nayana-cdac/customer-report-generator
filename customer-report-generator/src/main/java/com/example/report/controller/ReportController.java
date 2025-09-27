package com.example.report.controller;

import com.example.report.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{customerId}")
    public Map<String, Object> getReport(@PathVariable String customerId) {
        return reportService.generateReport(customerId);
    }
}

