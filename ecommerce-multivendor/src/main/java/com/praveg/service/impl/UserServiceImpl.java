package com.praveg.service.impl;

import com.praveg.config.JwtProvider;
import com.praveg.entity.User;
import com.praveg.repository.UserRepo;
import com.praveg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new Exception("User not found eith email -" +email);
        }
        return user;
    }
}
