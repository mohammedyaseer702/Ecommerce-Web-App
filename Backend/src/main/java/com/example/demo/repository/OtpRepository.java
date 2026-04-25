package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Otp;
import java.util.Optional;
public interface OtpRepository
        extends JpaRepository<Otp, Long> {

    Optional<Otp> findTopByPhoneOrderByIdDesc(String phone);
}

