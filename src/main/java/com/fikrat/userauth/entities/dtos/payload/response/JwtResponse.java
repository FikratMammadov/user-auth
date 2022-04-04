package com.fikrat.userauth.entities.dtos.payload.response;

import com.fikrat.userauth.entities.concretes.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private int id;
    private String username;
    private String email;
    private List<String> roles;
}
