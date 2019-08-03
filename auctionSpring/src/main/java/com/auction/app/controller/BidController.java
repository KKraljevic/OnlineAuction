package com.auction.app.controller;


import com.auction.app.repository.BidRepository;
import com.auction.app.conf.NotFoundException;
import com.auction.app.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "https://still-castle-19196.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class BidController {
    
    @Autowired
    private BidRepository bidRepository;

    @GetMapping("/bids")
    public ResponseEntity<List<Bid>> getAllBids() {
        List<Bid> bids = new ArrayList<Bid>();
        bidRepository.findAll().forEach(bids::add);
        if(bids.size()>0)
            return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
        else
            throw new NotFoundException("Bids not found!");
    }

    @GetMapping("/bids/{bidId}")
    public ResponseEntity<Bid> getBidById(@PathVariable Integer bidId) {
        if(!bidRepository.existsById(bidId)) {
            throw new NotFoundException("Bid not found!");
        }
        return new ResponseEntity<Bid>(bidRepository.findById(bidId).get(), HttpStatus.OK);
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
