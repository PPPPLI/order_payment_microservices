package com.cloud.spring.utils;

import com.cloud.spring.dto.UserDTO;
import com.cloud.spring.entity.vo.Authority;
import com.cloud.spring.entity.vo.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserUtil {


    public static User userToUser(UserDTO userDTO) {

        //String username = Arrays.toString(Base64.getDecoder().decode(userDTO.getUserName().getBytes()));
        //String password = Arrays.toString(Base64.getDecoder().decode(userDTO.getPassword().getBytes()));

        return User.builder()
                .username(userDTO.getUserName())
                .password(userDTO.getPassword())
                .authorities(Set.of(Authority.builder().authorityName("user").build()))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .build();
    }
}
