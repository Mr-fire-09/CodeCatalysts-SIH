## System Flow Walkthrough

1. **User submits application** from the citizen portal (`/api/citizen/applications`). Payload is stored in MySQL with a globally unique tracking ID.
2. **Application stored in MySQL** via JPA entity `Application`.
3. **Assigned to official** either automatically (round-robin hook ready) or through Admin endpoint `/api/admin/applications/{id}/assign`.
4. **AI monitors delays** through `AiEngineService.monitorAll()` scheduled job that evaluates every application.
5. **Alerts triggered** whenever `aiDelayScore ≥ 0.8` using `NotificationService`.
6. **Official updates progress** using `/api/official/applications/{id}/status`, updating timeline & AI metrics.
7. **Citizen gives rating** with OTP-backed `/api/feedback/{id}`.
8. **OTP verification** handled by `OtpService` verifying latest OTP record before persisting feedback.
9. **Hash stored on blockchain (sim)** once application reaches `APPROVED`/`AUTO_APPROVED`, `BlockchainService` anchors the document hash into the ledger.
10. **Dashboard shows insights** fetched from `/api/admin/dashboard` including counts, delay metrics, and performance scores.

