package dev.chari.playtestlog.dto.response;

import dev.chari.playtestlog.entity.ReportType;
import dev.chari.playtestlog.entity.Severity;
import java.time.Instant;

public record ReportResponse(
    Long id,
    Long sessionId,
    ReportType type,
    Severity severity,
    String description,
    Instant createdAt
) {}
