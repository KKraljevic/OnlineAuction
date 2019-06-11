package com.auction.app.controller;

import com.auction.app.BidRepository;
import com.auction.app.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class BidController {

    @Autowired
    private BidRepository bidRepository;

    @GetMapping("/bids")
    public List<Bid> getAllBids() {
        List<Bid> bids = new ArrayList<>();
        bidRepository.findAll().forEach(bids::add);
        return bids;
    }

}
