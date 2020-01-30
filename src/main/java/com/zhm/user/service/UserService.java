package com.zhm.user.service;

import com.zhm.user.domain.AppUser;
import com.zhm.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

// UserDetailsService specifies the methods required for getting users from db
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // this method is to allow spring security to get user from db and validate the username and password
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // get a user from db
        Optional<AppUser> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            // if user is present, convert the user into a User that spring uses
            AppUser u = user.get();
            return new User(
                    u.getEmail(), u.getEncryptedPassword(),
                    true, true, true, true,
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public AppUser createUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    public AppUser getUserByEmail(String email) {
        Optional<AppUser> appUser = userRepository.findByEmail(email);
        return appUser.orElseThrow(() -> new UsernameNotFoundException("User not found for " + email));
    }
}
