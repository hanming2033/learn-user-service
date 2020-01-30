package com.zhm.user.controller;

import com.zhm.user.domain.AppUser;
import com.zhm.user.dto.LoginRequestDTO;
import com.zhm.user.dto.LoginResponseDTO;
import com.zhm.user.dto.UserCreateRequestDTO;
import com.zhm.user.dto.UserResponseDTO;
import com.zhm.user.mapper.UserMapper;
import com.zhm.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private Environment env;
    private UserMapper userMapper;
    private UserService userService;
    private AuthenticationManager authManager;

    @Autowired
    public UsersController(Environment env, UserMapper userMapper, UserService userService, AuthenticationManager authManager) {
        this.env = env;
        this.userMapper = userMapper;
        this.userService = userService;
        this.authManager = authManager;
    }

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @GetMapping("/status/ping")
    public String status() {
        return "Ping on port " + env.getProperty("local.server.port") + " instance-id: " + instanceId;
    }

    @GetMapping("/protected")
    public String protectedRoute() {
        return "Protect : Ping on port " + env.getProperty("local.server.port") + " instance-id: " + instanceId;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) throws Exception {
        try {
            // convert the dto into an authentication token
            Authentication credential = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), new ArrayList<>());
            // for spring to authenticate based on the above token, spring has to get user data from db through UserService
            Authentication authResult = authManager.authenticate(credential);
            // read user details from db to attach info to response
            AppUser appUser = userService.getUserByEmail(dto.getEmail());
            // generate jwt
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", appUser.getEmail());
            claims.put("roles", Arrays.asList("normal-user","admin-user"));
            String jwtToken = Jwts.builder()
                    .setClaims(claims) // each claim will be a custom key value pair in the jwt body
                    .setSubject(appUser.getId().toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration"))))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                    .compact();
            return ResponseEntity.ok(new LoginResponseDTO(appUser.getId().toString(), appUser.getEmail(), jwtToken));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        AppUser appUser = userMapper.userCreateRequestDTOToUser(dto); // encryption of password is in the mapper
        AppUser storedAppUser = userService.createUser(appUser);
        UserResponseDTO userResponseDTO = userMapper.userToUserResponseDTO(storedAppUser);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }
}
