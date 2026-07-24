package com.slpolice.trafficfine.service;

import com.slpolice.trafficfine.dto.LoginRequest;
import com.slpolice.trafficfine.dto.RegisterRequest;
import com.slpolice.trafficfine.dto.AuthResponse;
import com.slpolice.trafficfine.dto.UserDto;
import com.slpolice.trafficfine.entity.User;
import com.slpolice.trafficfine.exception.DuplicateResourceException;
import com.slpolice.trafficfine.exception.UnauthorizedException;
import com.slpolice.trafficfine.repository.UserRepository;
import com.slpolice.trafficfine.security.JwtProvider;
import com.slpolice.trafficfine.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!user.getActive() || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole().name());
        UserDto userDto = userMapper.toDto(user);

        return AuthResponse.builder()
            .token(token)
            .user(userDto)
            .expiresIn(jwtProvider.getExpirationTime())
            .build();
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .phone(request.getPhone())
            .role(User.UserRole.DRIVER)
            .licenseNumber(request.getLicenseNumber())
            .vehicleRegistration(request.getVehicleRegistration())
            .district(request.getDistrict())
            .active(true)
            .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole().name());
        UserDto userDto = userMapper.toDto(user);

        return AuthResponse.builder()
            .token(token)
            .user(userDto)
            .expiresIn(jwtProvider.getExpirationTime())
            .build();
    }
}
