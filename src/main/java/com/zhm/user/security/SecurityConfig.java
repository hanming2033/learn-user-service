package com.zhm.user.security;

import com.zhm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${gateway.ip}")
    private String gatewayIp;

    private UserService userService;
    private BCryptPasswordEncoder bcrypt;
    private Environment env;

    @Autowired
    public SecurityConfig(UserService userService, BCryptPasswordEncoder bcrypt, Environment env) {
        this.userService = userService;
        this.bcrypt = bcrypt;
        this.env = env;
    }

    // configure spring http security
    // this service sits behind zuul api gateway service, jwt validation is done in zuul
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // disable cross origin check
        http.headers().frameOptions().disable(); // enable h2 console
        http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/**").hasIpAddress(gatewayIp); // only allow requests from a particular ip, zuul!
    }

    // configure spring auth builder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // spring will perform validation using authentication manager
        // authentication manager will call userService's loadUserByUsername method to retrieve the password
        // and use bcrypt to decode it ! user registration uses bcrypt to encode password
        auth.userDetailsService(userService).passwordEncoder(bcrypt);
    }

    // make authentication manager available to other components - just do it
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
