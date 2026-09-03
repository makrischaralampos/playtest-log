package dev.chari.playtestlog.controller;

import dev.chari.playtestlog.dto.request.CreateSessionRequest;
import dev.chari.playtestlog.dto.response.SessionResponse;
import dev.chari.playtestlog.service.PlaytestSessionService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final PlaytestSessionService sessionService;

    public SessionController(PlaytestSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(
        @Valid @RequestBody CreateSessionRequest request
    ) {
        SessionResponse created = sessionService.createSession(request);
        return ResponseEntity.created(
            URI.create("/sessions/" + created.id())
        ).body(created);
    }

    @GetMapping("/{id}")
    public SessionResponse getSession(@PathVariable Long id) {
        return sessionService.getSession(id);
    }

    @PatchMapping("/{id}/close")
    public SessionResponse closeSession(@PathVariable Long id) {
        return sessionService.closeSession(id);
    }
}
