package com.example.demo.service;
import com.example.demo.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import com.example.demo.entity.Otp;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;

    public String generateOtp(String phone) {

        String code = String.valueOf(
                new Random().nextInt(900000) + 100000);

        Otp otp = new Otp();
        otp.setPhone(phone);
        otp.setCode(code);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);

        otpRepository.save(otp);

        return code; // for now return in response
    }

    public String verifyOtp(String phone, String code) {

        Otp otp = otpRepository
                .findTopByPhoneOrderByIdDesc(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otp.isVerified())
            throw new RuntimeException("Already used");

        if (otp.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Expired");

        if (!otp.getCode().equals(code))
            throw new RuntimeException("Invalid OTP");

        otp.setVerified(true);
        otpRepository.save(otp);

        return "OTP verified";
    }
}

