package com.cloud.spring.service.impl;

import com.cloud.spring.entity.vo.Authority;
import com.cloud.spring.entity.vo.User;
import com.cloud.spring.repository.AuthorityRepository;
import com.cloud.spring.repository.UserRepository;
import com.cloud.spring.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    @Resource
    AuthorityRepository authorityRepository;

    @Override
    public User getUser(String userName) {

        return userRepository.findUserByUsername(userName);

    }

    @Override
    public User addUser(User user) {

        Authority authority = authorityRepository.findByAuthorityName("user");

        user.setAuthorities(Set.of(authority));

        return userRepository.save(user);
    }
}
