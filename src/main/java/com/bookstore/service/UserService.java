package com.bookstore.service;

import com.bookstore.dto.UserRequestDTO;
import com.bookstore.dto.UserResponseDTO;
import com.bookstore.entity.Customer;
import com.bookstore.entity.User;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.UserRequestMapper;
import com.bookstore.mapper.UserResponseMapper;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.JwtTokenService;
import com.bookstore.service.base.BaseService;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class UserService implements BaseService<UserRequestDTO, UserResponseDTO> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        User user = userRequestMapper.toEntity(userRequestDTO);

        commonValidation(user);
        requiredNullOrEmpty(user.getId(), "Id must be null");
        requiredTrue(userRepository.findByUsername(user.getUsername()).isEmpty(), String.format("User %s already exists", user.getUsername()));

        return userResponseMapper.toDTO(saveUserEncoded(user));
    }

    @Override
    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        User user = userRequestMapper.toEntity(userRequestDTO);

        commonValidation(user);
        requiredValue(user.getId(), "Id is required");

        return userResponseMapper.toDTO(saveUserEncoded(user));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO find(Long id) {
        return this.userRepository
                .findById(id)
                .map(userResponseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(String.format("User id %s not found", id)));
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return this.userRepository
                .findAll()
                .stream()
                .map(userResponseMapper::toDTO)
                .collect(Collectors.toList());
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

    public String login(UserResponseDTO userResponseDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userResponseDTO.username(), userResponseDTO.password());

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
