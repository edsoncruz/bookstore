package com.bookstore.security;

import com.bookstore.exception.BadRequestException;
import com.bookstore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsLoaderService implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailsLoaderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException("User was not found"));
    }

}
