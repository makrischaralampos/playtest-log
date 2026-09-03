package dev.chari.playtestlog.repository;

import dev.chari.playtestlog.entity.PlaytestSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaytestSessionRepository
    extends JpaRepository<PlaytestSession, Long>
{
    List<PlaytestSession> findByBuildVersion(String buildVersion);

    List<PlaytestSession> findByEndedAtIsNull();
}
