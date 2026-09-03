package dev.chari.playtestlog.service;

import dev.chari.playtestlog.dto.mapper.ReportMapper;
import dev.chari.playtestlog.dto.request.CreateReportRequest;
import dev.chari.playtestlog.dto.response.ReportResponse;
import dev.chari.playtestlog.entity.PlaytestSession;
import dev.chari.playtestlog.entity.Report;
import dev.chari.playtestlog.entity.ReportType;
import dev.chari.playtestlog.exception.SessionClosedException;
import dev.chari.playtestlog.repository.ReportRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final PlaytestSessionService sessionService;

    public ReportService(
        ReportRepository reportRepository,
        ReportMapper reportMapper,
        PlaytestSessionService sessionService
    ) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
        this.sessionService = sessionService;
    }

    @Transactional
    public ReportResponse addReport(
        Long sessionId,
        CreateReportRequest request
    ) {
        PlaytestSession session = sessionService.getSessionOrThrow(sessionId);
        if (!session.isOpen()) {
            throw new SessionClosedException(
                "Cannot add a report to closed session " + sessionId
            );
        }

        Report report = new Report(
            request.type(),
            request.description(),
            Instant.now()
        );
        report.setSeverity(request.severity());
        session.addReport(report);

        // session is already managed by this transaction's persistence context;
        // cascade on PlaytestSession.reports persists the new Report automatically
        return reportMapper.toResponse(report);
    }

    public List<ReportResponse> listReports(
        String buildVersion,
        ReportType type
    ) {
        List<Report> reports;

        if (buildVersion != null && type != null) {
            reports = reportRepository.findBySession_BuildVersionAndType(
                buildVersion,
                type
            );
        } else if (buildVersion != null) {
            reports = reportRepository.findBySession_BuildVersion(buildVersion);
        } else if (type != null) {
            reports = reportRepository.findByType(type);
        } else {
            reports = reportRepository.findAll();
        }

        return reports.stream().map(reportMapper::toResponse).toList();
    }
}
