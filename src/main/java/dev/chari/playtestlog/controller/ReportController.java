package dev.chari.playtestlog.controller;

import dev.chari.playtestlog.dto.request.CreateReportRequest;
import dev.chari.playtestlog.dto.response.ReportResponse;
import dev.chari.playtestlog.entity.ReportType;
import dev.chari.playtestlog.service.ReportService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/sessions/{sessionId}/reports")
    public ResponseEntity<ReportResponse> addReport(
        @PathVariable Long sessionId,
        @Valid @RequestBody CreateReportRequest request
    ) {
        ReportResponse created = reportService.addReport(sessionId, request);
        return ResponseEntity.created(
            URI.create("/reports/" + created.id())
        ).body(created);
    }

    @GetMapping("/reports")
    public List<ReportResponse> listReports(
        @RequestParam(required = false) String buildVersion,
        @RequestParam(required = false) ReportType type
    ) {
        return reportService.listReports(buildVersion, type);
    }
}
