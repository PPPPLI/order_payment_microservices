package com.cloud.spring.service.impl;

import com.cloud.spring.entity.vo.Authority;
import com.cloud.spring.repository.AuthorityRepository;
import com.cloud.spring.service.AuthorityService;
import jakarta.annotation.Resource;

public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthority(String authorityName) {

        return authorityRepository.findByAuthorityName(authorityName);
    }
}
