package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.entity.User;
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
public class UserController implements BaseController<User> {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    public User create(User user) {
        return this.userService.create(user) ;
    }

    @Override
    public User update(User user) {
        return this.userService.update(user);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.userService.delete(id);

        return new ResponseEntity<>(String.format("User id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public User find(Long id) {
        return this.userService.find(id);
    }

    @Override
    public List<User> findAll() {
        return this.userService.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String token = userService.login(user);

        return new ResponseEntity<>(String.format("Login success. Token: %s",token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user = userService.create(user);

        return new ResponseEntity<>(String.format("User %s created", user.getUsername()), HttpStatus.OK);
    }
}
