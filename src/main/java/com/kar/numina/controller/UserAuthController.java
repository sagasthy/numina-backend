package com.kar.numina.controller;

import com.kar.numina.model.*;
import com.kar.numina.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<User>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User user = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessResponse.<User>builder()
                            .message("User registered successfully")
                            .data(user)
                            .status(HttpStatus.CREATED.value())
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(SuccessResponse.<User>builder()
                            .message(e.getMessage())
                            .data(null)
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SuccessResponse.<User>builder()
                            .message("An error occurred during registration")
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SuccessResponse<User>> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok()
                    .body(SuccessResponse.<User>builder()
                            .message("User retrieved successfully")
                            .data(user)
                            .status(HttpStatus.OK.value())
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(SuccessResponse.<User>builder()
                            .message(e.getMessage())
                            .data(null)
                            .status(HttpStatus.NOT_FOUND.value())
                            .build());
        }
    }
}
