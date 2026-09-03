package dev.chari.playtestlog.dto.request;

import dev.chari.playtestlog.entity.ReportType;
import dev.chari.playtestlog.entity.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
    @NotNull(message = "type is required") ReportType type,

    Severity severity, // optional — only meaningful when type == BUG

    @NotBlank(message = "description is required") String description
) {}
