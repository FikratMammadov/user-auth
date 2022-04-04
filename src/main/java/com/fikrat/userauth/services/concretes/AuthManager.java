package com.fikrat.userauth.services.concretes;

import com.fikrat.userauth.dataAccess.RoleDao;
import com.fikrat.userauth.dataAccess.UserDao;
import com.fikrat.userauth.entities.concretes.Role;
import com.fikrat.userauth.entities.concretes.User;
import com.fikrat.userauth.entities.dtos.payload.request.LoginRequest;
import com.fikrat.userauth.entities.dtos.payload.request.RegisterRequest;
import com.fikrat.userauth.entities.dtos.payload.response.JwtResponse;
import com.fikrat.userauth.entities.enums.ERole;
import com.fikrat.userauth.security.jwt.JwtUtils;
import com.fikrat.userauth.security.services.UserDetailsImpl;
import com.fikrat.userauth.services.abstracts.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthManager implements AuthService {
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getUsername(),
                roles);
    }

    @Override
    public User register(RegisterRequest request) {
        if(userDao.existsByUsername(request.getUsername())){
            throw new RuntimeException();
        }

        if(userDao.existsByEmail(request.getEmail())){
            throw new RuntimeException();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();
        if(strRoles==null){
            Role userRole = roleDao.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }else{
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleDao.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleDao.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleDao.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userDao.save(user);
        return user;
    }
}
