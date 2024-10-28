package com.cloud.spring.controller;

import com.cloud.spring.dto.UserDTO;
import com.cloud.spring.entity.vo.User;
import com.cloud.spring.service.UserService;
import com.cloud.spring.utils.JwtUtil;
import com.cloud.spring.utils.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    @Resource
    ObjectMapper objectMapper;

    @Resource
    UserService userService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    AuthenticationManager authenticationManager;

    /**
     * @apiNote Login Api, first user must send his identity information, and it will be put into Security context for authentication use,
     * meanwhile it will search the username in the database, then do a comparison between these two identities.
     * If authentication passed, it will generate a one-hour token in order that user doesn't need re-login during next one hour. And
     * user also can request the another services with this token.
     *
     * @param user:{username,password} in json format
     * */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO user) {


        try {
            User newUser = UserUtil.userToUser(user);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getUsername(), newUser.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = JwtUtil.createJwtToken(objectMapper.writeValueAsString(newUser));
            log.info("{}- Token: {}", LocalDateTime.now(),token);

            return ResponseEntity.ok(token);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Please try later");
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


    /**
     * @apiNote Register Api, as its name, it's used to sign up. Also, if we process successfully, in
     * the end, use can get a generated one-hour token. By the way, before storing the password, we'll
     * encode it for security reason.
     *
     * @param  user:{username,password} in json format
     * @return ResponseEntity, variable in function of processing status
     * */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {

        log.info("user: {}", user);

        User newUser = UserUtil.userToUser(user);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if(userService.getUser(newUser.getUsername()) != null){

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Provided username is already taken");
        }


        if (userService.addUser(newUser) == null) {

            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Please try later");
        }

        newUser.setPassword(user.getPassword());

        try {
            String token = JwtUtil.createJwtToken(objectMapper.writeValueAsString(newUser));
            return ResponseEntity.ok(token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
