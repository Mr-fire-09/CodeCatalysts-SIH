# 🏛️ CodeCatalysts Digital Governance Platform

![License](https://img.shields.io/badge/license-MIT-green) ![Build](https://img.shields.io/badge/build-Maven-blue) ![Stack](https://img.shields.io/badge/stack-Spring%20Boot%20|%20MySQL%20|%20Vanilla%20JS-ff69b4) ![Status](https://img.shields.io/badge/SIH-ready-success)

Smart India Hackathon ready solution for citizen-centric service delivery. The platform digitizes application submission, AI-monitored workflows, blockchain-backed approvals, OTP verified feedback, delay analytics, and role-based dashboards.

---

## ✨ Highlight Features

- Citizen self-service portal with real-time tracking and OTP-backed feedback
- AI workflow engine (rule-based + schedulers) for delay detection and auto-approval after 30 days
- Notification, recommendation, and performance scoring services
- Blockchain hash-chain simulation anchoring approved documents
- Official productivity dashboard + Admin control center for assignments and configuration
- RESTful Spring Boot backend secured with JWT + Spring Security
- Responsive government-themed frontend (HTML/CSS/JS) ready for static hosting

---

## 🗂️ Folder Structure

```
CodeCatalysts-SIH
├── backend/                # Spring Boot project (controllers, services, AI, blockchain)
├── frontend/               # Static UI (HTML/CSS/JS)
├── docs/                   # Architecture, ER, API, AI, blockchain, deployment
├── scripts/                # (reserved) automation/deployment helpers
├── LICENSE
└── README.md
```

👉 Detailed diagrams + docs: `docs/architecture.md`, `docs/er-diagram.md`, `docs/api.md`, `docs/ai-engine.md`, `docs/blockchain-simulation.md`, `docs/deployment.md`

---

## 🚀 Quick Start

```bash
# Backend
cd backend
mvn clean spring-boot:run

# Frontend (static)
cd ../frontend
npx serve .
```

Configure MySQL credentials in `backend/src/main/resources/application.yml` or via environment variables. Default DB name: `digital_governance`.

---

## 🔐 Modules & APIs

| Module        | Highlights                                                                 | Sample Endpoint                          |
|---------------|-----------------------------------------------------------------------------|-----------------------------------------|
| Auth          | Citizen register/login, JWT-issued tokens                                   | `POST /api/auth/login`                  |
| Citizen       | Submit applications, track status, view timeline, feedback with OTP         | `POST /api/citizen/applications`        |
| Official      | Accept/Update applications, read feedback                                   | `POST /api/official/applications/{id}/status` |
| Admin         | Global monitoring, assignment, delay analytics, auto-approval config        | `GET /api/admin/dashboard`              |
| AI Engine     | Delay scoring, notifications, bottleneck tips, auto-approval scheduler      | `@Scheduled monitor`                    |
| Blockchain    | Hash-chain ledger for approved documents                                    | `BlockchainService.anchor()`            |

Full API catalogue 👉 `docs/api.md`

---

## 🧠 AI & Automation Flow

1. Citizen submits application → stored in MySQL with tracking ID.
2. Admin/Official assignment triggers AI watchlist.
3. AI Engine (`AiEngineService`) periodically scores inactivity, raises alerts, and auto-escalates.
4. After 30 days of inactivity (configurable) applications auto-approve and document hash anchors to the chain.
5. Citizens receive push/email notifications (simulated logs by `NotificationService`).
6. OTP-based feedback updates official performance scores that surface on dashboards.

Diagram 👉 `docs/ai-engine.md`

---

## ⛓️ Blockchain Simulation

- SHA-256 hash for each approved document.
- Chain persisted in `hash_records` table with pointers to previous hash.
- Exposed on frontend insights for public verifiability.

Details 👉 `docs/blockchain-simulation.md`

---

## 🛡️ Security

- Spring Security + JWT stateless auth
- Role-based access control (Citizen, Official, Admin)
- Global exception and validation handling
- Ready for HTTPS + reverse proxy deployment

---

## 📦 Deployment

1. Provision MySQL + (optional) Redis.
2. Build backend `mvn clean package` and run `java -jar target/digital-governance-platform-1.0.0.jar`.
3. Serve `frontend/` via CDN / Nginx / IIS static site.
4. Configure reverse proxy to route `/api` → backend, `/` → frontend.

More guidance 👉 `docs/deployment.md`

---

## 🤝 Contributing

1. Fork repo
2. Create feature branch `git checkout -b feature/xyz`
3. Commit with conventional message
4. Open PR with summary + screenshots

---

## 📜 License

Released under the MIT License. See `LICENSE` for details.