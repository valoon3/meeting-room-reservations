package com.app.wiseaiassignment.api.user.controller;

import com.app.wiseaiassignment.api.user.dto.JoinRequest;
import com.app.wiseaiassignment.api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Long join(@Valid @RequestBody JoinRequest request) {
        // Logic for user registration
        return userService.join(request);
    }

}
