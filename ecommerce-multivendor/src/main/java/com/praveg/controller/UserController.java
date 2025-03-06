package com.praveg.controller;

import com.praveg.entity.User;
import com.praveg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

   // Check
    @GetMapping("/api/users/profile")
    public ResponseEntity<User> userProfileHandler(@RequestHeader("Authorization") String jwt) throws Exception {
    User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }
}
