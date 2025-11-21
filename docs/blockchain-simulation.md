## Blockchain Hash Chain Simulation

To avoid the operational overhead of deploying a full blockchain network, the project ships with a lightweight hash-chain ledger:

1. Every approved or auto-approved application computes a SHA-256 hash of the document payload.
2. The hash is linked with the previous block hash to form an immutable chain (GENESIS → block₁ → block₂ ...).
3. The chain is persisted inside MySQL (`hash_records`) and exposed to dashboards for transparency.

### Block Structure

| Field         | Description                               |
|---------------|-------------------------------------------|
| documentHash  | Hash of application document metadata     |
| previousHash  | Chain pointer. `GENESIS` for the first    |
| chainHash     | SHA-256(documentHash + previousHash)      |
| applicationId | Reference to the approved application     |

### Pseudocode

```pseudo
previousHash = repository.findLatest()?.chainHash ?: "GENESIS"
chainHash = sha256(trackingId + documentHash + previousHash)
store(applicationId, documentHash, previousHash, chainHash)
```

You can later export the `hash_records` table and validate the integrity by recomputing hashes sequentially.

