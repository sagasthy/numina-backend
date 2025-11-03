package com.kar.numina.service;

import com.kar.numina.entity.User;
import com.kar.numina.model.UserRegistrationRequest;
import com.kar.numina.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public com.kar.numina.model.User registerUser(UserRegistrationRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        // Create user entity
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .currencyPref(request.getCurrencyPref() != null ? request.getCurrencyPref() : "USD")
                .timezone(request.getTimezone() != null ? request.getTimezone() : "UTC")
                .twoFactorEnabled(false)
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // Map entity to model
        return userEntityToModel(savedUser);
    }

    private static com.kar.numina.model.User userEntityToModel(User savedUser) {
        return com.kar.numina.model.User.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .currencyPref(savedUser.getCurrencyPref())
                .timezone(savedUser.getTimezone())
                .twoFactorEnabled(savedUser.getTwoFactorEnabled())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public com.kar.numina.model.User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return userEntityToModel(user);
    }

    public com.kar.numina.model.User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return userEntityToModel(user);
    }
}
