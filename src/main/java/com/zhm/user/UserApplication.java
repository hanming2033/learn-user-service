package com.zhm.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
public class UserApplication {

    @Bean
    public BCryptPasswordEncoder bCrypt(){
        return new BCryptPasswordEncoder();
    };

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
