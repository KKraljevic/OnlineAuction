package com.auction.app.controller;

import com.auction.app.AuctionItemsRepository;
import com.auction.app.model.AuctionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public AuctionItem getItemById(@PathVariable Integer id) {
        return itemRepository.findById(id).get();
    }

}
