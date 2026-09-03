package dev.chari.playtestlog.repository;

import dev.chari.playtestlog.entity.Report;
import dev.chari.playtestlog.entity.ReportType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findBySessionId(Long sessionId);

    List<Report> findByType(ReportType type);

    List<Report> findBySession_BuildVersionAndType(
        String buildVersion,
        ReportType type
    );

    List<Report> findBySession_BuildVersion(String buildVersion);
}
