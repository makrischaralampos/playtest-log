package dev.chari.playtestlog;

import dev.chari.playtestlog.entity.PlaytestSession;
import dev.chari.playtestlog.entity.Report;
import dev.chari.playtestlog.entity.ReportType;
import dev.chari.playtestlog.entity.Severity;
import dev.chari.playtestlog.repository.PlaytestSessionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // don't seed when running tests
public class SeedDataRunner implements CommandLineRunner {

    private final PlaytestSessionRepository sessionRepository;

    public SeedDataRunner(PlaytestSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void run(String... args) {
        if (sessionRepository.count() > 0) {
            return; // already seeded, e.g. on app restart with data still in the volume
        }

        Instant now = Instant.now();

        PlaytestSession closedSession = new PlaytestSession(
            "0.3.1-alpha",
            now.minus(2, ChronoUnit.DAYS)
        );
        closedSession.setNotes("Boss fight balance pass");
        closedSession.close(
            now.minus(2, ChronoUnit.DAYS).plus(45, ChronoUnit.MINUTES)
        );

        Report bug = new Report(
            ReportType.BUG,
            "Player falls through floor after dash-jump into corner",
            now.minus(2, ChronoUnit.DAYS)
        );
        bug.setSeverity(Severity.HIGH);
        closedSession.addReport(bug);

        Report feedback = new Report(
            ReportType.FEEDBACK,
            "Boss telegraph feels too fast to react to on first attempt",
            now.minus(2, ChronoUnit.DAYS)
        );
        closedSession.addReport(feedback);

        PlaytestSession openSession = new PlaytestSession(
            "0.4.0-alpha",
            now.minus(1, ChronoUnit.HOURS)
        );
        openSession.setNotes("Testing new dialogue system");

        Report reaction = new Report(
            ReportType.REACTION,
            "Laughed out loud at the merchant's second dialogue branch",
            now.minus(30, ChronoUnit.MINUTES)
        );
        openSession.addReport(reaction);

        sessionRepository.save(closedSession);
        sessionRepository.save(openSession);
    }
}
