## REST API Catalogue

All endpoints are prefixed with `/api`. JWT bearer tokens are required for protected routes.

### Auth

| Method | Endpoint             | Role      | Description                      |
|--------|----------------------|-----------|----------------------------------|
| POST   | `/auth/register`     | Public    | Citizen self-registration        |
| POST   | `/auth/login`        | Public    | Returns JWT + role claims        |

### Citizen

| Method | Endpoint                                | Description                          |
|--------|------------------------------------------|--------------------------------------|
| POST   | `/citizen/applications`                  | Submit a new application             |
| GET    | `/citizen/applications`                  | List citizen's applications          |
| GET    | `/citizen/applications/{trackingId}`     | Track application by ID              |
| GET    | `/citizen/applications/{trackingId}/timeline` | Status history timeline         |
| POST   | `/feedback/{trackingId}/otp`             | Initiate OTP for feedback            |
| POST   | `/feedback/{trackingId}`                 | Submit feedback + rating             |

### Official

| Method | Endpoint                                          | Description                 |
|--------|---------------------------------------------------|-----------------------------|
| GET    | `/official/applications`                          | List assigned apps          |
| POST   | `/official/applications/{trackingId}/status`      | Update status + remarks     |
| GET    | `/official/applications/{trackingId}/feedback`    | Read citizen feedback       |

### Admin

| Method | Endpoint                                              | Description                         |
|--------|-------------------------------------------------------|-------------------------------------|
| GET    | `/admin/applications`                                 | All applications                    |
| POST   | `/admin/applications/{trackingId}/assign`             | Assign to official                  |
| GET    | `/admin/delays`                                       | AI flagged delays                   |
| GET    | `/admin/dashboard`                                    | Performance dashboard summary       |
| POST   | `/admin/settings/auto-approval`                       | Update auto approval + delay config |

### Sample Payloads

```json
POST /api/citizen/applications
{
  "description": "Fire NOC request for Ward 12",
  "documentUrl": "https://files.gov/doc.pdf",
  "priority": 2
}
```

```json
POST /api/official/applications/DG-123/status
{
  "status": "APPROVED",
  "remarks": "All inspections passed"
}
```

