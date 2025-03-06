package com.praveg.service.impl;

import com.praveg.domain.USER_ROLE;
import com.praveg.entity.User;
import com.praveg.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    //** when we start backend automatically invoke initializeAdminUser method

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminUsername = "praveg@gmail.com";  // seller register email

        if(userRepo.findByEmail(adminUsername) == null){
            User adminUser = new User();
            adminUser.setPassword(passwordEncoder.encode("praveg5"));
            adminUser.setFullName("praveg");
            adminUser.setEmail(adminUsername);
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);

            userRepo.save(adminUser);
        }
    }
}
