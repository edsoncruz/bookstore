package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.dto.UserRequestDTO;
import com.bookstore.dto.UserResponseDTO;
import com.bookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements BaseController<UserRequestDTO, UserResponseDTO> {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        return this.userService.create(userRequestDTO) ;
    }

    @Override
    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        return this.userService.update(userRequestDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.userService.delete(id);

        return new ResponseEntity<>(String.format("User id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public UserResponseDTO find(Long id) {
        return this.userService.find(id);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return this.userService.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserResponseDTO userResponseDTO) {
        String token = userService.login(userResponseDTO);

        return new ResponseEntity<>(String.format("Login success. Token: %s",token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.create(userRequestDTO);

        return new ResponseEntity<>(String.format("User %s created", userResponseDTO.username()), HttpStatus.OK);
    }
}
