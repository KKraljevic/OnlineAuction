package com.auction.app.controller;

import com.auction.app.UserRepository;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public User findUser(@RequestBody User user){
        return userRepository.findByEmail(user.getEmail());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> findLogUser(@PathVariable("id") Integer id){
        User user=userRepository.findById(id).get();
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        System.out.println("Get all users...");

        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        return users;
    }


}
