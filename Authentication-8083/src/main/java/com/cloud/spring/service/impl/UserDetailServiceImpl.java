package com.cloud.spring.service.impl;

import com.cloud.spring.entity.vo.User;
import com.cloud.spring.entity.vo.UserSecurity;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUser(username);

        if(user == null) throw new UsernameNotFoundException("User does not exist");

        UserSecurity userSecurity = new UserSecurity(user);
        userSecurity.setAuthorities(user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthorityName())).toList());

        return userSecurity;
    }
}
