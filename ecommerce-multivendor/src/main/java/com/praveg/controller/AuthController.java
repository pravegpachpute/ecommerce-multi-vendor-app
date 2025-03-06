package com.praveg.controller;

import com.praveg.domain.USER_ROLE;
import com.praveg.entity.VerificationCode;
import com.praveg.repository.UserRepo;
import com.praveg.request.LoginOtpRequest;
import com.praveg.request.LoginRequest;
import com.praveg.request.SignupRequest;
import com.praveg.response.ApiResponse;
import com.praveg.response.AuthResponse;
import com.praveg.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final AuthService authService;

    // check
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Registration Successfully...");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    // check
    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpRequest req) throws Exception {

        authService.sendLoginOtp(req.getEmail(), req.getRole());
        ApiResponse response = new ApiResponse();
        response.setMessage("Otp send Successfully...");

        return ResponseEntity.ok(response);
    }

    // check
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signinHandler(@RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }
}
