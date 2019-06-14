package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.model.AuctionItem;
import com.auction.app.model.Bid;
import com.auction.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ItemController {

    @Autowired
    private AuctionItemsRepository itemRepository ;

    @GetMapping("/items")
    public List<AuctionItem> getAllItems() {
        List<AuctionItem> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }
    @GetMapping("/item/{id}")
    public ResponseEntity<AuctionItem> getItemById(@PathVariable Integer id) {
        AuctionItem item = new AuctionItem ();
        item=itemRepository.findById(id).get();
        return new ResponseEntity<AuctionItem>(item, HttpStatus.OK);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<AuctionItem> updateItem(@PathVariable("id") Integer id, @RequestBody AuctionItem item) {
            AuctionItem newItem= new AuctionItem();
            newItem=itemRepository.findById(id).get();
            newItem.setCurrentPrice(item.getCurrentPrice());
            return new ResponseEntity<AuctionItem>(itemRepository.save(newItem), HttpStatus.OK);
    }
}
