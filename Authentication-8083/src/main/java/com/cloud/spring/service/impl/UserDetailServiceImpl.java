package com.cloud.spring.service.impl;

import com.cloud.spring.entity.vo.User;
import com.cloud.spring.entity.vo.UserSecurity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Exclusive Service in the Spring Security framework to verify the identity of user
 * */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUser(username);

        log.info("User: {}",user);

        if(user == null) {

            log.error("User does not exist");
            throw new UsernameNotFoundException("User does not exist");
        }

        UserSecurity userSecurity = new UserSecurity(user);
        userSecurity.setAuthorities(user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthorityName())).toList());

        log.info("UserDetails: {}", userSecurity);
        return userSecurity;
    }
}
