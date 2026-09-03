package dev.chari.playtestlog.dto.mapper;

import dev.chari.playtestlog.dto.response.SessionResponse;
import dev.chari.playtestlog.entity.PlaytestSession;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionResponse toResponse(PlaytestSession session) {
        return new SessionResponse(
            session.getId(),
            session.getBuildVersion(),
            session.getStartedAt(),
            session.getEndedAt(),
            session.isOpen(),
            session.getNotes()
        );
    }
}
