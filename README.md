# Playtest Log

A self-hosted REST API for logging personal game playtest sessions — bug reports,
feedback, and reactions, tagged by build version.

## Why this exists

Most playtest/analytics tooling (itch.io analytics, third-party crash/feedback SDKs,
Steam Playtest telemetry) assumes you want aggregate, anonymized data flowing through
someone else's infrastructure. For a solo dev testing your own builds, that's the
wrong shape of tool: you don't need anonymization from yourself, you don't want a
vendor dependency for something this small, and you'd rather own the data outright
than trust a third party's retention policy.

Playtest Log is the alternative: a small API you run on your own machine (or your
own server, if you want it there), backed by a database you control, with a schema
simple enough to read in five minutes. No account creation, no SDK to integrate into
your game client, no data leaving your machine unless you decide to move it.

## Stack

- Java 25, Spring Boot 4.1
- Spring Data JPA + MySQL 8
- Docker Compose (for local MySQL)

## Data model

- **Session** — one playtest sitting, tagged with the build version tested
  (`startedAt`, `endedAt`, `notes`)
- **Report** — a single bug, piece of feedback, or reaction logged during a session
  (`type`: `BUG` / `FEEDBACK` / `REACTION`, optional `severity`, `description`)

A session has many reports. Closing a session is explicit (`PATCH /sessions/{id}/close`)
rather than an implicit side effect — once closed, it stops accepting new reports.

## Running locally

Requires Docker and JDK 25.

```fish
# Start MySQL
docker compose up -d

# Run the app (default port 8080)
mvn spring-boot:run
```

On first run, `ddl-auto: update` creates the schema automatically and a seed runner
populates a couple of example sessions/reports — see `SeedDataRunner`. There's no
migration tool (Flyway/Liquibase) by design for this project's scope; see
[Design decisions](#design-decisions) below.

## API

| Method | Endpoint                       | Description                          |
| ------ | ------------------------------ | ------------------------------------ |
| POST   | `/sessions`                    | Start a new playtest session         |
| GET    | `/sessions/{id}`               | Get a session by id                  |
| PATCH  | `/sessions/{id}/close`         | Close an open session                |
| POST   | `/sessions/{id}/reports`       | Log a report against an open session |
| GET    | `/reports?buildVersion=&type=` | List reports, optionally filtered    |

Example — start a session, log a bug, close it:

```fish
curl -X POST http://localhost:8080/sessions \
  -H "Content-Type: application/json" \
  -d '{"buildVersion": "0.4.1-alpha", "notes": "Testing new dialogue system"}'

curl -X POST http://localhost:8080/sessions/1/reports \
  -H "Content-Type: application/json" \
  -d '{"type": "BUG", "severity": "MEDIUM", "description": "Save icon overlaps HUD at 21:9"}'

curl -X PATCH http://localhost:8080/sessions/1/close
```

Errors return a consistent shape:

```json
{
  "timestamp": "2026-09-03T10:15:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Cannot add a report to closed session 1"
}
```

## Design decisions

A few choices made deliberately for this project's actual scope (solo use), worth stating rather than leaving implicit:

- **No auth.** Single-operator tool, not multi-tenant. Adding auth would be solving
  a problem this project doesn't have.
- **No migration tool.** `ddl-auto: update` is fine for a single dev, single
  environment. Would switch to Flyway before this ever ran anywhere shared.
- **`buildVersion` is a plain string, not a foreign key to a `BuildVersion` table.**
  Simpler, and the only user is the one entering the values.
- **No analytics/aggregation endpoints.** This is a log, not a dashboard. Aggregation
  is a natural v2 once there's real data to aggregate over.

## License

MIT — do whatever you want with it.
