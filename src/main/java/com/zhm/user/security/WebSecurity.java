package com.zhm.user.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Value("${gateway.ip}")
    private String ipAddr;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // disable cross origin check
        http.headers().frameOptions().disable(); // enable h2 console
        http.authorizeRequests().antMatchers("/**").hasIpAddress(ipAddr); // only allow requests from a particular ip, zuul!
    }
}
