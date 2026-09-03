package dev.chari.playtestlog.dto.response;

import java.time.Instant;

public record SessionResponse(
    Long id,
    String buildVersion,
    Instant startedAt,
    Instant endedAt,
    boolean open,
    String notes
) {}
