package com.bookstore.service;

import com.bookstore.entity.Customer;
import com.bookstore.entity.User;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.JwtTokenService;
import com.bookstore.service.base.BaseService;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class UserService implements BaseService<User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public User create(User user) {
        commonValidation(user);
        requiredNullOrEmpty(user.getId(), "Id must be null");
        requiredTrue(userRepository.findByUsername(user.getUsername()).isEmpty(), String.format("User %s already exists", user.getUsername()));

        return saveUserEncoded(user);
    }

    @Override
    public User update(User user) {
        commonValidation(user);
        requiredValue(user.getId(), "Id is required");

        return saveUserEncoded(user);
    }

    public User save(User user){
        Optional<User> userOptional = userRepository.findById(user.getId());

        if(userOptional.isPresent()){
            return update(user);
        }else {
            return create(user);
        }
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.userRepository.deleteById(id);
    }

    @Override
    public User find(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);

        if(userOptional.isPresent()) {
            return userOptional.get();
        }else {
            throw new NotFoundException(String.format("User id %s not found", id));
        }
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    private void commonValidation(User user){
        requiredValue(user, "User is required");
        requiredValue(user.getUsername(), "Username is required");
        requiredValue(user.getPassword(), "Password is required");
        requiredValue(user.getRole(), "Role is required");
    }

    public User saveUserEncoded(User user){
        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        return userRepository.save(user);
    }

    public String login(User user){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User userDetails = (User) authentication.getPrincipal();

        return jwtTokenService.generateToken(userDetails);
    }

    public User getAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();

            return findByUsername(userName);
        }
        return null;
    }

    public void validateAuthorization(Customer customer){
        User userAuthenticated = getAuthenticated();

        if(!customer.getUser().getId().equals(userAuthenticated.getId())){
            throw new BadRequestException("User don`t have authorization");
        }

    }

    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isPresent()) {
            return userOptional.get();
        }else {
            throw new NotFoundException(String.format("User %s not found", username));
        }
    }
}
