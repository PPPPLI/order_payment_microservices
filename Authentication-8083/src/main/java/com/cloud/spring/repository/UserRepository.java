package com.cloud.spring.repository;

import com.cloud.spring.entity.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserByUsername(String username);
}
