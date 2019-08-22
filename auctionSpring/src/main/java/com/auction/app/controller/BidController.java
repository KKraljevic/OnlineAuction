package com.auction.app.controller;


import com.auction.app.model.Bid;
import com.auction.app.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService){
        this.bidService=bidService;
    }

    @GetMapping("/bids/{bidId}")
    public ResponseEntity<Bid> getBidById(@PathVariable Integer bidId) {
        return new ResponseEntity<Bid>(bidService.getBidById(bidId), HttpStatus.OK);
    }

    @PutMapping("/bids/{bidId}")
    public ResponseEntity<Bid> updateBid(@PathVariable Integer bidId, @Valid @RequestBody Bid bidUpdated) {
        bidService.updateBid(bidUpdated);
        return new ResponseEntity<>(bidUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<Object> deleteBid(@PathVariable Integer bidId) {
        bidService.deleteBid(bidId);
        return new ResponseEntity<>("Bid is deleted successsfully", HttpStatus.OK);
    }
}
