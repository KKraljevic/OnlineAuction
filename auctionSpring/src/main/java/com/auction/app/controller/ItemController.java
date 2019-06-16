package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.UserRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.AuctionItem;
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
public class ItemController {

    @Autowired
    private AuctionItemsRepository itemRepository ;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/items")
    public List<AuctionItem> getAllItems() {
        List<AuctionItem> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }
    
    @GetMapping("/item/{id}")
    public ResponseEntity<AuctionItem> getItemById(@PathVariable Integer id) {
        if(!itemRepository.existsById(id)) {
            throw new NotFoundException("Item not found!");
        }
        return new ResponseEntity<AuctionItem>(itemRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/items")
    public ResponseEntity<List<AuctionItem>>getUsersItems(@PathVariable Integer userId) {

        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found!");
        }
        return new ResponseEntity<List<AuctionItem>>(itemRepository.findBySellerId(userId), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/items")
    public AuctionItem addAuctionItem(@PathVariable Integer userId, @Valid @RequestBody AuctionItem item) {
        return userRepository.findById(userId)
                .map(user -> {
                    item.setSeller(user);
                    return itemRepository.save(item);
                }).orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @PutMapping("/items/{itemId}")
    public AuctionItem updateAuctionItem(@PathVariable Integer itemId, @Valid @RequestBody AuctionItem itemUpdated) {

        return itemRepository.findById(itemId)
                .map(item -> {
                    item.setCurrentPrice(itemUpdated.getCurrentPrice());
                    item.setDescription(itemUpdated.getDescription());
                    item.setEndDate(itemUpdated.getEndDate());
                    item.setStartPrice(itemUpdated.getStartPrice());
                    return itemRepository.save(item);
                }).orElseThrow(() -> new NotFoundException("Auction item not found!"));
    }

    @DeleteMapping("/items/{itemId}")
    public String deleteAuctionItem(@PathVariable Integer itemId) {

        return itemRepository.findById(itemId)
                .map(item -> {
                    itemRepository.delete(item);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Auction item not found!"));
    }

}
