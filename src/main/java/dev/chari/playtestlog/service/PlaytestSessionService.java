package dev.chari.playtestlog.service;

import dev.chari.playtestlog.dto.mapper.SessionMapper;
import dev.chari.playtestlog.dto.request.CreateSessionRequest;
import dev.chari.playtestlog.dto.response.SessionResponse;
import dev.chari.playtestlog.entity.PlaytestSession;
import dev.chari.playtestlog.exception.ResourceNotFoundException;
import dev.chari.playtestlog.exception.SessionClosedException;
import dev.chari.playtestlog.repository.PlaytestSessionRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaytestSessionService {

    private final PlaytestSessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    public PlaytestSessionService(
        PlaytestSessionRepository sessionRepository,
        SessionMapper sessionMapper
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request) {
        PlaytestSession session = new PlaytestSession(
            request.buildVersion(),
            Instant.now()
        );
        session.setNotes(request.notes());
        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Transactional
    public SessionResponse closeSession(Long id) {
        PlaytestSession session = getSessionOrThrow(id);
        if (!session.isOpen()) {
            throw new SessionClosedException(
                "Session " + id + " is already closed"
            );
        }
        session.close(Instant.now());
        return sessionMapper.toResponse(session); // no explicit save needed — see note below
    }

    public SessionResponse getSession(Long id) {
        return sessionMapper.toResponse(getSessionOrThrow(id));
    }

    /**
     * Package-private: used by ReportService to attach reports to a live,
     * managed entity — not just a mapped DTO.
     */
    PlaytestSession getSessionOrThrow(Long id) {
        return sessionRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Session not found: " + id)
            );
    }
}
