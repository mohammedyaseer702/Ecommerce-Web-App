package com.example.demo.service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    // STEP 1: Generate reset token
    @Transactional
    public String generateResetToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // delete old tokens
        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        return token; // later send by email
    }

    // STEP 2: Reset password
    @Transactional
    public String resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        
        tokenRepository.delete(resetToken);

        return "Password updated successfully";
    }
}
