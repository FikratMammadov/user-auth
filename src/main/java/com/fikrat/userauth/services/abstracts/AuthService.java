package com.fikrat.userauth.services.abstracts;

import com.fikrat.userauth.entities.concretes.User;
import com.fikrat.userauth.entities.dtos.payload.request.LoginRequest;
import com.fikrat.userauth.entities.dtos.payload.request.RegisterRequest;
import com.fikrat.userauth.entities.dtos.payload.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    User register(RegisterRequest request);
}
