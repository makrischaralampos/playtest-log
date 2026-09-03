package dev.chari.playtestlog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSessionRequest(
    @NotBlank(message = "buildVersion is required")
    @Size(max = 50, message = "buildVersion must be 50 characters or fewer")
    String buildVersion,

    @Size(max = 5000, message = "notes must be 5000 characters or fewer")
    String notes
) {}
