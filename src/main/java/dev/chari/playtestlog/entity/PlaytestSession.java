package dev.chari.playtestlog.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class PlaytestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "build_version", nullable = false, length = 50)
    private String buildVersion;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(
        mappedBy = "session",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Report> reports = new ArrayList<>();

    protected PlaytestSession() {
        // JPA requires a no-arg constructor
    }

    public PlaytestSession(String buildVersion, Instant startedAt) {
        this.buildVersion = buildVersion;
        this.startedAt = startedAt;
    }

    public void close(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public boolean isOpen() {
        return endedAt == null;
    }

    public void addReport(Report report) {
        reports.add(report);
        report.setSession(this);
    }

    // getters (no public setters for id/startedAt — see note below)
    public Long getId() {
        return id;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public String getNotes() {
        return notes;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
