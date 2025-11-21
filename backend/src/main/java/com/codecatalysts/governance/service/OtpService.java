package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.OTPRecord;
import com.codecatalysts.governance.entity.User;
import com.codecatalysts.governance.repository.OTPRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OTPRecordRepository otpRecordRepository;
    private final NotificationService notificationService;
    private final Random random = new Random();

    public OTPRecord issueOtp(Application application, User citizen) {
        String otp = String.format("%06d", random.nextInt(999999));
        OTPRecord record = OTPRecord.builder()
                .application(application)
                .citizen(citizen)
                .otpCode(otp)
                .channel("SMS")
                .expiry(LocalDateTime.now().plusMinutes(10))
                .build();
        otpRecordRepository.save(record);
        notificationService.notifyCitizen(citizen, "Your feedback OTP is " + otp);
        return record;
    }

    public boolean verifyOtp(Application application, String otpCode) {
        return otpRecordRepository.findByApplication_Id(application.getId())
                .filter(record -> !record.isVerified())
                .filter(record -> record.getExpiry().isAfter(LocalDateTime.now()))
                .filter(record -> record.getOtpCode().equals(otpCode))
                .map(record -> {
                    record.setVerified(true);
                    otpRecordRepository.save(record);
                    return true;
                })
                .orElse(false);
    }
}

