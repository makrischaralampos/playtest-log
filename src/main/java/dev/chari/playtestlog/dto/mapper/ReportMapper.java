package dev.chari.playtestlog.dto.mapper;

import dev.chari.playtestlog.dto.response.ReportResponse;
import dev.chari.playtestlog.entity.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportResponse toResponse(Report report) {
        return new ReportResponse(
            report.getId(),
            report.getSession().getId(),
            report.getType(),
            report.getSeverity(),
            report.getDescription(),
            report.getCreatedAt()
        );
    }
}
