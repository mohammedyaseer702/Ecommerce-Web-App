package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    // ================= REGISTER =================

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        String roleName = request.getRole() != null ? request.getRole() : "USER";

Role role = roleRepository.findByName(roleName)
        .orElseThrow(() ->
                new ResourceNotFoundException("Role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    // ================= AUTHENTICATE =================

    public User authenticate(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid credentials"));

        if (!encoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new UnauthorizedException("Invalid credentials");
        }

        return user;
    }
}
