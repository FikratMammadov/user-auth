package com.fikrat.userauth.controllers;


import com.fikrat.userauth.entities.concretes.User;
import com.fikrat.userauth.entities.dtos.payload.request.LoginRequest;
import com.fikrat.userauth.entities.dtos.payload.request.RegisterRequest;
import com.fikrat.userauth.entities.dtos.payload.response.JwtResponse;
import com.fikrat.userauth.entities.dtos.payload.response.MessageResponse;
import com.fikrat.userauth.services.abstracts.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*",maxAge = 3600)
@RequiredArgsConstructor
public class AuthController{
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        MessageResponse messageResponse = new MessageResponse("User registered successfully");
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
