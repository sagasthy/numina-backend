package com.kar.numina.service;

import com.kar.numina.entity.User;
import com.kar.numina.model.LoginResponse;
import com.kar.numina.model.UserLoginRequest;
import com.kar.numina.model.UserRegistrationRequest;
import com.kar.numina.repository.UserRepository;
import com.kar.numina.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${numina.default-timezone:America/New_York}")
    private String defaultTimezone;

    // Defaults & messages consolidated for maintainability
    private static final String DEFAULT_CURRENCY = "CAD";
    private static final String PASSWORD_POLICY_MESSAGE = "Password must be at least 8 characters, contain upper, lower, digit, and special character.";
    private static final String USER_EXISTS_MESSAGE = "User with email %s already exists";
    private static final String USER_ID_NOT_FOUND_MESSAGE = "User not found with id: %d";
    private static final String USER_EMAIL_NOT_FOUND_MESSAGE = "User not found with email: %s";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";

    // Password regex matches previous logic: at least one upper, lower, digit, special char, min length 8
    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");

    @Transactional
    public com.kar.numina.model.User registerUser(UserRegistrationRequest request) {
        validatePasswordStrength(request.getPassword());
        ensureEmailNotExists(request.getEmail());

        User entityToPersist = buildUserEntity(request);
        User saved = userRepository.save(entityToPersist);
        return mapToModel(saved);
    }

    private void validatePasswordStrength(String password) {
        if (!StringUtils.hasLength(password) || !STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(PASSWORD_POLICY_MESSAGE);
        }
    }

    private void ensureEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(String.format(USER_EXISTS_MESSAGE, email));
        }
    }

    private User buildUserEntity(UserRegistrationRequest request) {
        return User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .currencyPref(request.getCurrencyPref() != null ? request.getCurrencyPref() : DEFAULT_CURRENCY)
                .timezone(request.getTimezone() != null ? request.getTimezone() : defaultTimezone)
                .twoFactorEnabled(false)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private static com.kar.numina.model.User mapToModel(User entity) {
        return com.kar.numina.model.User.builder()
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .currencyPref(entity.getCurrencyPref())
                .timezone(entity.getTimezone())
                .twoFactorEnabled(entity.getTwoFactorEnabled())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public com.kar.numina.model.User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_ID_NOT_FOUND_MESSAGE, userId)));
        return mapToModel(user);
    }

    public com.kar.numina.model.User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_EMAIL_NOT_FOUND_MESSAGE, email)));
        return mapToModel(user);
    }

    public LoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE);
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .userId(user.getUserId())
                .build();
    }

    // Legacy loop implementation replaced by regex for clarity & equivalence.
    
}