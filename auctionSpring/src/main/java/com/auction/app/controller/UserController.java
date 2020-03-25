package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.BidRepository;
import com.auction.app.CategoryRepository;
import com.auction.app.UserRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemsRepository itemsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BidRepository bidRepository;

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
        if(users.size()>0)
            return users;
        else
            throw new NotFoundException("No users found");
    }

    @GetMapping("/users/{userId}/items")
    public ResponseEntity<List<AuctionItem>>getUsersItems(@PathVariable Integer userId) {

        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found!");
        }
        List<AuctionItem> items= new ArrayList<>();
        items=itemsRepository.findBySellerId(userId);
        if(items.size()>0)
            return new ResponseEntity<List<AuctionItem>>(items,HttpStatus.OK);
        else throw new NotFoundException("Items not found!");
    }

    @PostMapping("/users/{userId}/items/{catId}")
    public AuctionItem addAuctionItem(@PathVariable Integer userId,@PathVariable Integer catId, @Valid @RequestBody AuctionItem item) {
        userRepository.findById(userId)
                .map(user -> {
                    item.setSeller(user);
                    return true;
                }).orElseThrow(() -> new NotFoundException("User not found!"));
        categoryRepository.findById(catId).map(
                cat -> {
                    item.setCategory(cat);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Category not found!"));
        return itemsRepository.save(item);
    }

    @GetMapping("/users/{userId}/bids")
    public ResponseEntity<List<Bid>> getUserBids(@PathVariable Integer userId) {
        List<Bid> bids = new ArrayList<Bid>();
        bidRepository.findByBidderId(userId).forEach(bids::add);
        if(bids.size()>0)
            return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
        else
            throw new NotFoundException("Bids not found!");
    }

    @PostMapping("/users/{userId}/bids/item/{itemId}")
    public Bid addBid(@PathVariable Integer userId, @PathVariable Integer itemId, @Valid @RequestBody Bid bid) {
        userRepository.findById(userId).map(user -> {
            bid.setBidder(user);
            return user;
        }).orElseThrow(() -> new NotFoundException("Bidder not found!"));
        itemsRepository.findById(itemId).map(item -> {
            if (item.getCurrentPrice()<bid.getBidPrice()) {
                item.setCurrentPrice(bid.getBidPrice());
                itemsRepository.save(item);
            }
            bid.setAuctionItem(item);
            return item;
        }).orElseThrow(() -> new NotFoundException("Item not found!"));
        return bidRepository.save(bid);
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
