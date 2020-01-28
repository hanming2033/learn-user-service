package com.zhm.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhm.user.dto.LoginRequestDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>());
            return getAuthenticationManager().authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
