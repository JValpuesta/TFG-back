package com.valpuestajorge.conecta4.security;


import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findUserByUsername(username).block();
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("Username " + username +" not found");
        }
        return new User(user.getUsername(), user.getPassword(), user.getUserAvailableFlag(), user.getAccountNotExpired(),
                user.getCredentialNotExpired(), user.getAccountNotLocked(), authorityList );
    }
}
