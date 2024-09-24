package com.cloud.spring.service;

import com.cloud.spring.entity.vo.User;

public interface UserService {

    User getUser(String userName);

    User addUser(User user);
}
