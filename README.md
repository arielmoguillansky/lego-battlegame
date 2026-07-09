# LEGO Battlegame

A Battleship-style ("Salvo") multiplayer game, themed around **LEGO Marvel's
Avengers**. Built during an advanced full-stack web development course: a Java
**Spring Boot** backend serving a jQuery/HTML/CSS front-end.

## Tech stack

- **Backend:** Java, Spring Boot 2.7, Spring MVC (REST API), Spring Data JPA
  (Hibernate), Spring Data REST, Spring Security (form login + delegating
  password hashing)
- **Database:** H2 (in-memory)
- **Front-end:** HTML, CSS, JavaScript, jQuery
- **Build & run:** Gradle, Docker

## Features

- Login / sign-up with Spring Security
- Games lobby with an all-time scorers leaderboard
- Turn-based battle grid — place ships, fire salvoes, track hits
- REST API (`/api/**`) plus Spring Data REST (`/rest`)

## Run locally

### With Docker (recommended)

```bash
docker build -t lego-battlegame .
docker run -p 8080:8080 lego-battlegame
```

Then open http://localhost:8080/web/login.html

The app seeds demo data on startup (in-memory; resets on every restart), so you
can sign up for a new account or explore the games lobby right away.

### Without Docker

Requires JDK 17 and Gradle 7.x.

```bash
gradle bootRun
```

## Deploy

The included `Dockerfile` is self-contained (multi-stage Gradle build → Temurin
runtime) and binds to `$PORT`, so it deploys to any Docker-friendly host —
Render, Railway, Fly.io or Google Cloud Run. No database setup is needed
(in-memory H2).

On [Render](https://render.com): **New → Web Service → connect this repo**; the
Dockerfile is auto-detected — no extra configuration required.
