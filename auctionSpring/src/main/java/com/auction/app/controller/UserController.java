package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.BidRepository;
import com.auction.app.UserRepository;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    @PostMapping("/login")
    public User findUser(@RequestBody User user) {
        return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> findLogUser(@PathVariable Integer id) {
        User user = userRepository.findById(id).get();
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public User addUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            return null;
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

    @GetMapping("/bids/{id}")
    public List<Bid> getUserBids(@PathVariable Integer id) {
        return userRepository.findById(id).get().getBids();
    }

    @PostMapping("/createBid")
    public Bid makeBid(@RequestBody Bid bid) {
        Bid b=bidRepository.save(new Bid(bid.getBid_Id(),bid.getBidPrice(),bid.getBidTime(),bid.isActive(),bid.getUser(),bid.getItem()));
        return b;
    }


}
