package com.praveg.service;

import com.praveg.domain.USER_ROLE;
import com.praveg.request.LoginRequest;
import com.praveg.request.SignupRequest;
import com.praveg.response.AuthResponse;

public interface AuthService {

     String createUser(SignupRequest req) throws Exception;

     //send login and signup otp method
     void sendLoginOtp(String email, USER_ROLE role) throws Exception;

     //login method
     AuthResponse signing(LoginRequest req) throws Exception;
}
