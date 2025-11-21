package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.HashRecord;
import com.codecatalysts.governance.repository.HashRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final HashRecordRepository hashRecordRepository;

    public HashRecord anchor(Application application) {
        String previousHash = hashRecordRepository.findTopByOrderByCreatedAtDesc()
                .map(HashRecord::getChainHash)
                .orElse("GENESIS");

        String data = application.getTrackingId() + application.getDocumentHash() + previousHash;
        String chainHash = sha256(data);

        HashRecord record = HashRecord.builder()
                .application(application)
                .documentHash(application.getDocumentHash())
                .previousHash(previousHash)
                .chainHash(chainHash)
                .build();
        return hashRecordRepository.save(record);
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String hexString = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) {
                    hex.append('0');
                }
                hex.append(hexString);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to hash document", e);
        }
    }
}

