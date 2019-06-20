package com.auction.app.controller;


import com.auction.app.AuctionItemsRepository;
import com.auction.app.BidRepository;
import com.auction.app.UserRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class BidController {
    
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionItemsRepository itemRepository ;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/bids")
    public ResponseEntity<List<Bid>> getAllBids() {
        List<Bid> bids = new ArrayList<Bid>();
        bidRepository.findAll().forEach(bids::add);
        if(bids.size()>0)
            return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
        else
            throw new NotFoundException("Bids not found!");
    }

    @GetMapping("/bids/{userId}")
    public ResponseEntity<List<Bid>> getUserBids(@PathVariable Integer userId) {
        List<Bid> bids = new ArrayList<Bid>();
        bidRepository.findByBidderId(userId).forEach(bids::add);
        if(bids.size()>0)
            return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
        else
            throw new NotFoundException("Bids not found!");
    }

    @GetMapping("/bids/item/{itemId}")
    public ResponseEntity<List<Bid>> getItemBids(@PathVariable Integer itemId) {
        List<Bid> bids = new ArrayList<Bid>();
        bidRepository.findByAuctionItemId(itemId).forEach(bids::add);
        if(bids.size()>0)
            return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
        else
            throw new NotFoundException("Bids not found!");
    }

    @PostMapping("/bids/{userId}/{itemId}")
    public Bid addBid(@PathVariable Integer userId, @PathVariable Integer itemId, @Valid @RequestBody Bid bid) {
        userRepository.findById(userId).map(user -> {
            bid.setBidder(user);
            return user;
        }).orElseThrow(() -> new NotFoundException("Bidder not found!"));
        itemRepository.findById(itemId).map(item -> {
            if (item.getCurrentPrice()<bid.getBidPrice()) {
                item.setCurrentPrice(bid.getBidPrice());
                itemRepository.save(item);
            }
            bid.setAuctionItem(item);
            return item;
        }).orElseThrow(() -> new NotFoundException("Item not found!"));
        return bidRepository.save(bid);
    }

    @PutMapping("/bids/{bidId}")
    public Bid updateBid(@PathVariable Integer bidId, @Valid @RequestBody Bid bidUpdated) {

        return bidRepository.findById(bidId)
                .map(bid -> {
                    bid.setBidPrice(bidUpdated.getBidPrice());
                    bid.setActive(bidUpdated.isActive());
                    bid.setBidTime(bidUpdated.getBidTime());
                    return bidRepository.save(bid);
                }).orElseThrow(() -> new NotFoundException("Bid not found!"));
    }

    @DeleteMapping("/bids/{bidId}")
    public String deleteBid(@PathVariable Integer bidId) {

        return bidRepository.findById(bidId)
                .map(bid -> {
                    bidRepository.delete(bid);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Bid not found!"));
    }
}
