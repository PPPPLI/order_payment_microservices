package com.cloud.spring.repository;

import com.cloud.spring.entity.vo.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    Authority findByAuthorityName(String authorityName);
}
