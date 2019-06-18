package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.BidRepository;
import com.auction.app.UserRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public User findUser(@RequestBody User user) {
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @GetMapping("/users/{id}")
    public User findLogUser(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found with id " + id);
        }
    }
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new NotFoundException("Email is taken");
        else {
            userRepository.save(user);
            return user;
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User userUpdated) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userUpdated.getFirstName());
                    user.setLastName(userUpdated.getLastName());
                    user.setPassword(userUpdated.getPassword());
                    user.setEmail(userUpdated.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return "Delete Successfully!";
                }).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

}
