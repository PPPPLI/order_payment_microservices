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
import java.util.List;

@Component
@Slf4j
public class WebSecurityFilter extends OncePerRequestFilter {

    @Resource
    ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if(request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/register")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if(token == null || !token.startsWith("Bearer ")) {

            printResponse(response,ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login"));
            return;
        }

        token = token.replace("Bearer ", "");

        if(!JwtUtil.verifyToken(token)) {

            printResponse(response,ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
            return;
        }

        String userInfo = JwtUtil.getUserInfoFromJWT(token);
        User user = objectMapper.readValue(userInfo, User.class);
        List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthorityName())).toList();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    public void printResponse(HttpServletResponse resp, ResponseEntity<String> message) {

        try {
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
