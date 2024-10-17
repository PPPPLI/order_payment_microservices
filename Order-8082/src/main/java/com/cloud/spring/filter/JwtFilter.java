package com.cloud.spring.filter;

import com.cloud.spring.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.io.PrintWriter;

/**
 * Token check filter, apply to verify the user's authentication in the first moment
 * */
@WebFilter("/*")
public class JwtFilter implements Filter {

    @Resource
    ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //If URI == /order/api-doc, let the request pass directly
        if(request.getRequestURI().equals("/order/api-doc")) {

            filterChain.doFilter(request, response);
            return;
        }

        //Get authorization data from header
        String token = request.getHeader("Authorization");


        //If the authorization data doesn't exist, refuse the request
        if(token == null || !token.startsWith("Bearer ")) {

            printResponse(response, ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login"));
            return;
        }

        //Extract the token information
        token = token.replace("Bearer ", "");

        //Verify the token
        if(!JwtUtil.verifyToken(token)) {

            printResponse(response,ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
            return;
        }

        //If token is valid, get it passed
        filterChain.doFilter(request, response);
    }

    //Generate the refuse response
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
