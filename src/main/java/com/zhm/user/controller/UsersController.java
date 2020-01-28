package com.zhm.user.controller;

import com.zhm.user.domain.User;
import com.zhm.user.dto.UserCreateRequestDTO;
import com.zhm.user.dto.UserResponseDTO;
import com.zhm.user.mapper.UserMapper;
import com.zhm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment env;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @GetMapping("/status/ping")
    public String status() {
        return "Ping on port " + env.getProperty("local.server.port") + " instance-id: " + instanceId;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        User user = userMapper.userCreateRequestDTOToUser(dto);
        User storedUser = userService.createUser(user);
        UserResponseDTO userResponseDTO = userMapper.userToUserResponseDTO(storedUser);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }
}
