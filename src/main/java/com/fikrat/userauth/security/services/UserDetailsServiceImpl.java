package com.fikrat.userauth.security.services;

import com.fikrat.userauth.dataAccess.UserDao;
import com.fikrat.userauth.entities.concretes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found with username: "+username));
        return UserDetailsImpl.build(user);
    }
}
