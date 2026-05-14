package com.example.polls.controller;

import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.model.User;
import com.example.polls.model.Role;
import com.example.polls.model.RoleName;
import java.util.Collections;
import com.example.polls.repository.RoleRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider,
                          RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByUsernameOrEmail(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getUsernameOrEmail()
                )
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String jwt = tokenProvider.generateToken(user.getId());

        return new JwtAuthenticationResponse(jwt);
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return "Username is already taken!";
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Email is already in use!";
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                "{noop}" + signUpRequest.getPassword()
        );

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User role not set"));

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return "User registered successfully!";
    }

}