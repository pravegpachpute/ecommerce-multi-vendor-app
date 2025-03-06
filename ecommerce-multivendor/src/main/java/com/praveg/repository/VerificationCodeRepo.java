package com.praveg.repository;

import com.praveg.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepo extends JpaRepository<VerificationCode , Long> {

    VerificationCode findByEmail(String email);

    VerificationCode findByOtp(String otp);
}
