package com.cloud.spring.filter;

import com.cloud.spring.entity.vo.User;
import com.cloud.spring.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Authentication filter, in use of checking available token in the first moment when requests enter
 * */
@Component
@Slf4j
public class WebSecurityFilter extends OncePerRequestFilter {

    @Resource
    ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();
        //If request uri corresponds to these three types below, we let just the request pass to the next filter without checking
        if(uri.equals("/auth/login") || uri.equals("/auth/register") || uri.equals("/auth/api-doc") || uri.equals("/actuator/health")) {

            filterChain.doFilter(request, response);
            return;
        }

        //Get the authorization information in header
        String token = request.getHeader("Authorization");

        //If the token is not available, the request will be refused
        if(token == null || !token.startsWith("Bearer ")) {

            printResponse(response,ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login"));
            return;
        }

        //Extract the valid token information
        token = token.replace("Bearer ", "");

        //Verify the token
        if(!JwtUtil.verifyToken(token)) {

            printResponse(response,ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
            return;
        }

        //Extract the user identity information from the token
        String userInfo = JwtUtil.getUserInfoFromJWT(token);
        User user = objectMapper.readValue(userInfo, User.class);
        List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthorityName())).toList();

        //Put relative user information like username, password into security context for authentication use
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        log.info("{} - Token is verified", LocalDateTime.now());

        //The verification passed
        filterChain.doFilter(request, response);
    }

    //Generate a appropriate response in case of failure
    public void printResponse(HttpServletResponse resp, ResponseEntity<String> message) {

        try {

            log.error("{} - Token is invalid", LocalDateTime.now());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String response = objectMapper.writeValueAsString(message);
            PrintWriter out = resp.getWriter();
            out.print(response);
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
