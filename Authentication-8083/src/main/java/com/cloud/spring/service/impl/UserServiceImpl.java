package com.cloud.spring.service.impl;

import com.cloud.spring.entity.vo.User;
import com.cloud.spring.repository.UserRepository;
import com.cloud.spring.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    @Override
    public User getUser(String userName) {

        return userRepository.findUserByUsername(userName);

    }

    @Override
    public User addUser(User user) {

        return userRepository.save(user);
    }
}
